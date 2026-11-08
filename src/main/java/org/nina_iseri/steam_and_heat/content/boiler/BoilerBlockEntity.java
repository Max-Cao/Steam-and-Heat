package org.nina_iseri.steam_and_heat.content.boiler;

import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import org.nina_iseri.steam_and_heat.client.ModParticles;
import org.nina_iseri.steam_and_heat.content.IHeatReceiver;
import org.nina_iseri.steam_and_heat.content.burner.BurnerBlock;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;
import org.nina_iseri.steam_and_heat.utils.FluidUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public abstract class BoilerBlockEntity extends SmartBlockEntity implements IHeatReceiver, IHaveGoggleInformation {
    public FluidTank input = FluidUtils.single_fluid_tank(16000, Fluids.WATER);
    public FluidTank output = FluidUtils.single_fluid_tank(16000, ModFluids.STEAM.get());

    private IFluidHandler fluid_handler = new IFluidHandler() {
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
                case 0 -> 16000;
                case 1 -> 16000;
                default -> 0;
            };
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            if (tank == 0 && stack.getFluid() == Fluids.WATER)
                return true;
            // the steam tank should not input
            return false;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int filled = input.fill(resource, action);
            if (filled > 0 && action == FluidAction.EXECUTE) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            return filled;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack drained = output.drain(resource, action);
            if (!drained.isEmpty() && action == FluidAction.EXECUTE) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            return drained;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack drained = output.drain(maxDrain, action);
            if (!drained.isEmpty() && action == FluidAction.EXECUTE) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            return drained;
        }
    };

    int heat_total;
    int heat_last;

    public static final ICapabilityProvider<BoilerBlockEntity, Direction, IFluidHandler> FLUID_CAP = (
            BoilerBlockEntity te, Direction side) -> {
        return te.fluid_handler;
    };

    public BoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        nbt.put("in", input.writeToNBT(registries, new CompoundTag()));
        nbt.put("out", output.writeToNBT(registries, new CompoundTag()));
        nbt.putInt("hu", heat_total);
        nbt.putInt("lasthu", heat_last);
        super.write(nbt, registries, clientPacket);
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        input.readFromNBT(registries, nbt.getCompound("in"));
        output.readFromNBT(registries, nbt.getCompound("out"));
        heat_total = nbt.getInt("hu");
        heat_last = nbt.getInt("lasthu");
        super.read(nbt, registries, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        if (this.level == null)
            return;
        if (!this.level.isClientSide) {
            getHeatFromHeater();
            heat_last = heat_total;
            if (heat_total != 0) {
                int consume = Math.min(getHUPerTick(), heat_total);
                heat_total = 0;
                double waterconsume = (ModCommonConfig.COMMON.steamPerWater.get() * 10);
                consume = Math.min((int) (this.input.drain((int) Math.ceil(consume / waterconsume), FluidAction.EXECUTE)
                        .getAmount() * waterconsume), consume);
                this.output.fill(new FluidStack(ModFluids.STEAM.get(), consume / 10),
                        FluidAction.EXECUTE);
                if (consume > 0) {
                    this.setChanged();
                    this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
                }
            }

        } else {
            if (output.getFluidAmount() >= output.getCapacity() && heat_last != 0) {// steam leaking
                particleInterval++;
                if (particleInterval >= 20) {
                    particleInterval = 0;
                    double d0 = this.worldPosition.getX();
                    double d1 = this.worldPosition.getY() + 1;
                    double d2 = this.worldPosition.getZ();
                    // if(p_180655_4_.nextDouble()<0.5D) {

                    this.level.playLocalSound(d0, d1, d2, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25F,
                            0.25F, false);

                    for (int i = 0; i < this.level.random.nextInt(3) + 1; i++)
                        this.level.addParticle(ModParticles.STEAM.get(), d0 + this.level.random.nextFloat(), d1,
                                d2 + this.level.random.nextFloat(), 0.0D, 0.0D, 0.0D);
                    // }
                }
            } else
                particleInterval = 0;
        }
    }

    private int particleInterval = 0;

    @Override
    public void commitHeat(float value) {
        heat_total = (int) value;
    }

    protected void getHeatFromHeater() {
        BlockPos below = this.getBlockPos().below();
        BlockState belowState = this.level.getBlockState(below);
        if (belowState.getBlock() instanceof BurnerBlock)
            return;
        float heat = BoilerHeater.findHeat(this.level, below, belowState);
        if (heat > 0) {
            commitHeat(Math.max(heat, 3f) * getHUPerTick() * 0.75f);
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        this.containedFluidTooltip(tooltip, isPlayerSneaking, input);
        this.containedFluidTooltip(tooltip, isPlayerSneaking, output);
        return true;
    }

    protected abstract int getHUPerTick();
}
