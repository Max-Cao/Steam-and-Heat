package org.nina_iseri.steam_and_heat.content.engine.steam_engine;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;

import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.client.ModParticles;
import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlockEntity;
import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlock;
import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlockEntity;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;
import org.nina_iseri.steam_and_heat.registrate.ModTags;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

/**
 * 蒸汽引擎方块实体的公共父类。
 *
 * <p>工作流程：</p>
 * <ol>
 *     <li>优先消耗自身储罐中的蒸汽；自身储罐为空时，从正后方的锅炉抽取。</li>
 *     <li>蒸汽充足时先“预热” {@link #HEATUP_TICKS} tick，然后才开始输出转速。</li>
 *     <li>蒸汽耗尽后转速归零，并重新进入预热状态。</li>
 * </ol>
 *
 * <p>具体等级的数值（转速、应力、耗汽量等）由子类通过抽象方法提供。</p>
 */
public abstract class SteamEngineBlockEntity extends CommonEngineBlockEntity implements IHaveGoggleInformation {

    /** 从开始耗汽到真正输出动力所需的 tick 数。 */
    private static final int HEATUP_TICKS = 60;
    /** 开始工作前至少需要积攒的“可运行 tick 数”，避免蒸汽断续导致频繁启停。 */
    private static final int MIN_STEAM_TICKS = 40;
    /** 客户端蒸汽粒子的发射间隔（tick）。 */
    private static final int PARTICLE_INTERVAL = 40;

    private final FluidTank tank;
    private final IFluidHandler fluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tankIndex) {
            return tank.getFluid();
        }

        @Override
        public int getTankCapacity(int tankIndex) {
            return tank.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tankIndex, FluidStack stack) {
            return tank.isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int filled = tank.fill(resource, action);
            if (filled > 0 && action == FluidAction.EXECUTE) {
                markUpdated();
            }
            return filled;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            // 蒸汽只能通过引擎自身消耗，不对外开放抽取。
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }
    };

    private int heatup;
    private int particleInterval;

    public static final ICapabilityProvider<SteamEngineBlockEntity, Direction, IFluidHandler> FLUID_CAP =
            (blockEntity, side) -> blockEntity.fluidHandler;

    public SteamEngineBlockEntity(BlockEntityType<? extends SteamEngineBlockEntity> type, BlockPos pos,
            BlockState state) {
        super(type, pos, state);
        this.tank = new FluidTank(getSteamStorage(), fluidStack -> {
            if (ModTags.STEAM != null) {
                return fluidStack.is(ModTags.STEAM);
            }
            return fluidStack.getFluid() == ModFluids.STEAM.get();
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) {
            return;
        }
        if (level.isClientSide) {
            tickClient();
        } else {
            tickServer();
        }
    }

    /**
     * 服务端逻辑：连接飞轮、抽取蒸汽、预热并更新输出。
     */
    private void tickServer() {
        BlockState state = getBlockState();

        if (poweredWheel == null) {
            attachWheel();
        }

        // 没有飞轮时引擎不会输出动力，但仍然会缓慢排空自身储罐（与原实现一致）。
        if (poweredWheel == null || poweredWheel.isRemoved()) {
            setAppliedRotation(0, 0);
            heatup = 0;
            if (!tank.drain(getSteamConsumptionPerTick(), IFluidHandler.FluidAction.EXECUTE).isEmpty()) {
                markUpdated();
            }
            setLit(state, false);
            return;
        }

        Direction engineFacing = state.getValue(CommonEngineBlock.FACING);
        BlockPos boilerPos = worldPosition.relative(engineFacing, -1);
        FluidTank steamSource = tank;
        if (tank.isEmpty()) {
            BlockEntity blockEntity = level.getBlockEntity(boilerPos);
            if (blockEntity instanceof BoilerBlockEntity boiler) {
                steamSource = boiler.output;
            }
        }

        int consumption = getSteamConsumptionPerTick();

        // 刚开始工作时先攒够蒸汽，避免抽一口就停。
        if (heatup == 0 && consumption > 0
                && steamSource.getFluidAmount() / consumption < MIN_STEAM_TICKS) {
            setAppliedRotation(0, 0);
            setLit(state, false);
            return;
        }

        boolean hasSteam = consumption <= 0
                || (!steamSource.isEmpty()
                        && steamSource.drain(consumption, IFluidHandler.FluidAction.EXECUTE).getAmount() >= consumption);

        if (hasSteam) {
            if (heatup >= HEATUP_TICKS) {
                float speed = getGeneratingSpeed();
                float capacity = getGeneratingCapacity();
                // 从锅炉直接抽汽会损失一部分效率。
                if (steamSource != tank) {
                    capacity = Mth.ceil(capacity * getSuckEfficiency());
                }
                setAppliedRotation(speed, capacity);
            } else {
                heatup++;
                markUpdated();
                setLit(state, true);
            }
        } else {
            if (heatup > 0) {
                heatup--;
                markUpdated();
            }
            setAppliedRotation(0, 0);
            setLit(state, false);
        }
    }

    /**
     * 客户端逻辑：只负责在引擎运行时生成蒸汽粒子。
     */
    private void tickClient() {
        BlockState state = getBlockState();
        if (!state.getValue(SteamEngineBlock.LIT)) {
            particleInterval = 0;
            return;
        }

        if (++particleInterval < PARTICLE_INTERVAL) {
            return;
        }
        particleInterval = 0;
        spawnSteamParticles(state);
    }

    private void spawnSteamParticles(BlockState state) {
        double x = worldPosition.getX() + 0.5D;
        double y = worldPosition.getY();
        double z = worldPosition.getZ() + 0.5D;

        level.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25F, 0.25F, false);

        Direction direction = state.getValue(CommonEngineBlock.FACING);
        Direction.Axis axis = direction.getAxis();
        double randomOffset = level.random.nextDouble() * 0.6D - 0.3D;
        double dx = axis == Direction.Axis.X ? direction.getStepX() * 0.52D : randomOffset;
        double dy = level.random.nextDouble() * 9.0D / 16.0D;
        double dz = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52D : randomOffset;

        level.addParticle(ModParticles.STEAM.get(), x + dx, y + dy, z + dz, 0.0D, 0.0D, 0.0D);
    }

    /**
     * 修改 LIT 方块状态；只有状态真正变化时才触发方块更新。
     */
    private void setLit(BlockState state, boolean lit) {
        if (state.getValue(SteamEngineBlock.LIT) == lit) {
            return;
        }
        level.setBlockAndUpdate(worldPosition, state.setValue(SteamEngineBlock.LIT, lit));
    }

    private void markUpdated() {
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!getBlockState().getValue(SteamEngineBlock.LIT)) {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.steam_engine.not_enough_steam")
                    .style(ChatFormatting.RED).forGoggles(tooltip, 1);
        } else if (heatup < HEATUP_TICKS) {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.steam_engine.heating")
                    .style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
        } else {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.steam_engine.running")
                    .style(ChatFormatting.GREEN).forGoggles(tooltip, 1);
        }
        return containedFluidTooltip(tooltip, isPlayerSneaking, fluidHandler);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        tank.readFromNBT(registries, compound.getCompound("TankContent"));
        heatup = compound.getInt("heatup");
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("TankContent", tank.writeToNBT(registries, new CompoundTag()));
        compound.putInt("heatup", heatup);
    }

    /**
     * 该等级引擎产生的应力容量。
     */
    public abstract float getGeneratingCapacity();

    /**
     * 该等级引擎产生的转速。
     */
    public abstract float getGeneratingSpeed();

    /**
     * 每 tick 消耗的蒸汽量（mB）。
     */
    public abstract int getSteamConsumptionPerTick();

    /**
     * 自身储罐容量（mB）。
     */
    public abstract int getSteamStorage();

    /**
     * 从锅炉直接抽汽时的效率系数。
     */
    public abstract double getSuckEfficiency();
}
