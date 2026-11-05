package org.nina_iseri.steam_and_heat.content.boiler;

import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.nina_iseri.steam_and_heat.SPConfig;
import org.nina_iseri.steam_and_heat.client.Particles;
import org.nina_iseri.steam_and_heat.content.burner.BurnerBlock;
import org.nina_iseri.steam_and_heat.content.burner.IHeatReceiver;
import org.nina_iseri.steam_and_heat.registrate.SPFluids;
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

public abstract class BoilerTileEntity extends SmartBlockEntity implements IHeatReceiver, IHaveGoggleInformation {
    FluidTank input = new FluidTank(10000,s->s.getFluid() == Fluids.WATER);
    public FluidTank output = new FluidTank(10000);
    private IFluidHandler ft = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 2;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            switch (tank) {
                case 0:
                    return input.getFluid();
                case 1:
                    return output.getFluid();
                default:
                    return null;
            }
        }

        @Override
        public int getTankCapacity(int tank) {
            return 10000;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            if (tank == 0 && stack.getFluid() == Fluids.WATER)
                return true;
            return false;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int filled=input.fill(resource, action);
            if(filled>0&&action==FluidAction.EXECUTE) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            return filled;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack drained=output.drain(resource, action);
            if(!drained.isEmpty()&&action==FluidAction.EXECUTE){
                setChanged();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            return drained;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack drained=output.drain(maxDrain, action);
            if(!drained.isEmpty()&&action==FluidAction.EXECUTE){
                setChanged();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
            return drained;
        }
    };
    int heatreceived;
    int lastheat;

    public static final ICapabilityProvider<BoilerTileEntity, Direction, IFluidHandler> FLUID_CAP = (BoilerTileEntity te, Direction side) -> {
        return te.ft;
    };

    public BoilerTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        nbt.put("in", input.writeToNBT(registries, new CompoundTag()));
        nbt.put("out", output.writeToNBT(registries, new CompoundTag()));
        nbt.putInt("hu", heatreceived);
        nbt.putInt("lasthu", lastheat);
        super.write(nbt, registries, clientPacket);
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        input.readFromNBT(registries, nbt.getCompound("in"));
        output.readFromNBT(registries, nbt.getCompound("out"));
        heatreceived = nbt.getInt("hu");
        lastheat=nbt.getInt("lasthu");
        super.read(nbt, registries, clientPacket);
    }

//    @Override
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        CompoundTag nbt = new CompoundTag();
//        this.writeCustomNBT(nbt);
//        return ClientboundBlockEntityDataPacket.create(this);
//    }
//
//    @Override
//    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//        this.readCustomNBT(pkt.getTag());
//    }
//
//    @Override
//    public CompoundTag getUpdateTag() {
//        CompoundTag nbt = super.getUpdateTag();
//        writeCustomNBT(nbt);
//        return nbt;
//    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {

        if (this.level == null)
            return;
        if (!this.level.isClientSide) {
            getHeatFromHeater();
            lastheat = heatreceived;
            if (heatreceived != 0) {
                int consume = Math.min(getHUPerTick(), heatreceived);
                heatreceived = 0;
                double waterconsume = (SPConfig.COMMON.steamPerWater.get() * 10);
                consume = Math.min((int) (this.input.drain((int) Math.ceil(consume / waterconsume), FluidAction.EXECUTE)
                        .getAmount() * waterconsume), consume);
                this.output.fill(new FluidStack(SPFluids.STEAM.get(), consume / 10),
                        FluidAction.EXECUTE);
                if(consume>0) {
                    this.setChanged();
                    this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
                }
            }

        } else {
            if (output.getFluidAmount() >= output.getCapacity() && lastheat != 0) {// steam leaking
                particleInterval++;
                if (particleInterval >= 20) {
                    particleInterval = 0;
                    double d0 = this.worldPosition.getX();
                    double d1 = this.worldPosition.getY() + 1;
                    double d2 = this.worldPosition.getZ();
                    // if(p_180655_4_.nextDouble()<0.5D) {

                    this.level.playLocalSound(d0, d1, d2, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25F,
                            0.25F, false);

                    for(int i=0;i<this.level.random.nextInt(3)+1;i++)
                        this.level.addParticle(Particles.STEAM.get(), d0 + this.level.random.nextFloat(), d1,
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
        heatreceived = (int) value;

    }

    protected void getHeatFromHeater() {
        BlockPos below = this.getBlockPos().below();
        BlockState belowState = this.level.getBlockState(below);
        if (belowState.getBlock() instanceof BurnerBlock) return;
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
