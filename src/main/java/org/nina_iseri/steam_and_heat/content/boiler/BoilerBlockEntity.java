package org.nina_iseri.steam_and_heat.content.boiler;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import org.nina_iseri.steam_and_heat.client.ModParticles;
import org.nina_iseri.steam_and_heat.content.heat.HeatGoggles;
import org.nina_iseri.steam_and_heat.content.heat.HeatHandler;
import org.nina_iseri.steam_and_heat.content.heat.IHeatHandler;
import org.nina_iseri.steam_and_heat.content.recipe.BoilerRecipe;
import org.nina_iseri.steam_and_heat.content.recipe.ModRecipes;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;
import org.nina_iseri.steam_and_heat.utils.FluidUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

/**
 * 锅炉方块实体。
 *
 * <p>每 tick 的 HU/水消耗与蒸汽产量由 {@link BoilerRecipe} 提供。锅炉本身只负责
 * 热量、水和蒸汽的存储与传输。</p>
 */
public abstract class BoilerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public final FluidTank input = FluidUtils.single_fluid_tank(16000, Fluids.WATER);
    public final FluidTank output = FluidUtils.single_fluid_tank(16000, ModFluids.STEAM.get());

    private final IFluidHandler fluid_handler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 2;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return switch (tank) {
                case 0 -> input.getFluid();
                case 1 -> output.getFluid();
                default -> null;
            };
        }

        @Override
        public int getTankCapacity(int tank) {
            return switch (tank) {
                case 0, 1 -> 16000;
                default -> 0;
            };
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            // 蒸汽罐只允许抽出，不允许输入。
            return tank == 0 && stack.getFluid() == Fluids.WATER;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int filled = input.fill(resource, action);
            if (filled > 0 && action == FluidAction.EXECUTE) {
                markUpdated();
            }
            return filled;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack drained = output.drain(resource, action);
            if (!drained.isEmpty() && action == FluidAction.EXECUTE) {
                markUpdated();
            }
            return drained;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack drained = output.drain(maxDrain, action);
            if (!drained.isEmpty() && action == FluidAction.EXECUTE) {
                markUpdated();
            }
            return drained;
        }
    };

    private final HeatHandler heat = new HeatHandler(320000, 16000, 0);
    private BoilerRecipe recipe;

    public static final ICapabilityProvider<BoilerBlockEntity, Direction, IFluidHandler> FLUID_CAP =
            (blockEntity, side) -> blockEntity.fluid_handler;

    public static final ICapabilityProvider<BoilerBlockEntity, Void, IHeatHandler> HEAT_CAP =
            (blockEntity, side) -> blockEntity.heat;

    public BoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected BoilerRecipe getRecipe() {
        if (recipe == null && level != null) {
            recipe = ModRecipes.findBoiler(level, new ItemStack(getBlockState().getBlock())).orElse(null);
        }
        return recipe;
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        nbt.put("in", input.writeToNBT(registries, new CompoundTag()));
        nbt.put("out", output.writeToNBT(registries, new CompoundTag()));
        nbt.put("heat", heat.serializeNBT(registries));
        super.write(nbt, registries, clientPacket);
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        input.readFromNBT(registries, nbt.getCompound("in"));
        output.readFromNBT(registries, nbt.getCompound("out"));
        heat.deserializeNBT(registries, nbt.get("heat"));
        super.read(nbt, registries, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void tick() {
        if (level == null) {
            return;
        }

        if (level.isClientSide) {
            tickClient();
            return;
        }

        BoilerRecipe recipe = getRecipe();
        if (recipe == null) {
            return;
        }

        if (heat.getHeatAmount() < recipe.huPerTick() || input.getFluidAmount() < recipe.waterPerTick()) {
            return;
        }

        heat.exactInner(recipe.huPerTick(), false);
        input.drain(recipe.waterPerTick(), FluidAction.EXECUTE);
        output.fill(new FluidStack(ModFluids.STEAM.get(), recipe.steamPerTick()), FluidAction.EXECUTE);
        markUpdated();
    }

    private void tickClient() {
        // 输出罐满且仍有热量时，从顶部冒出蒸汽。
        if (output.getFluidAmount() < output.getCapacity() || heat.noHeat()) {
            particleInterval = 0;
            return;
        }

        if (++particleInterval < 20) {
            return;
        }
        particleInterval = 0;

        double x = worldPosition.getX();
        double y = worldPosition.getY() + 1;
        double z = worldPosition.getZ();

        level.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25F, 0.25F, false);
        for (int i = 0; i < level.random.nextInt(3) + 1; i++) {
            level.addParticle(ModParticles.STEAM.get(), x + level.random.nextFloat(), y,
                    z + level.random.nextFloat(), 0.0D, 0.0D, 0.0D);
        }
    }

    private void markUpdated() {
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    private int particleInterval = 0;

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        HeatGoggles.addToTooltip(tooltip, heat);
        containedFluidTooltip(tooltip, isPlayerSneaking, input);
        containedFluidTooltip(tooltip, isPlayerSneaking, output);
        return true;
    }
}
