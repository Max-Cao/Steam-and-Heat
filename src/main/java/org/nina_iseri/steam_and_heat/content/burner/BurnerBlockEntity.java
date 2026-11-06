package org.nina_iseri.steam_and_heat.content.burner;

import java.util.List;

import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.nina_iseri.steam_and_heat.ModCommonConfig;

import org.nina_iseri.steam_and_heat.oldcreatestuff.IGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public abstract class BurnerBlockEntity extends SmartBlockEntity implements IGoggleInformation {
    private ItemStackHandler inv = new ItemStackHandler() {

        @Override
        public boolean isItemValid(int slot,ItemStack stack) {
            return stack.getBurnTime(RecipeType.SMELTING) != 0 && stack.getCraftingRemainingItem().isEmpty();
        }

    };
    int HURemain;

    public static final ICapabilityProvider<BurnerBlockEntity, Direction, IItemHandler> ITEM_CAP = (BurnerBlockEntity te, Direction side) -> {
        return te.inv;
    };

    public BurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        inv.deserializeNBT(registries, nbt.getCompound("inv"));
        HURemain = nbt.getInt("hu");
        super.read(nbt, registries, clientPacket);
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        nbt.put("inv", inv.serializeNBT(registries));
        nbt.putInt("hu", HURemain);
        super.write(nbt, registries, clientPacket);
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

    public void tick() {
        if (level != null && !level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            int emit = getHuPerTick();
            while (HURemain < emit && this.consumeFuel()) ;
            if (HURemain < emit) {
                if (HURemain > 0) {
                    emitHeat(HURemain);
                    HURemain = 0;
                    this.setChanged();
                    this.level.sendBlockUpdated(worldPosition, state, state, 3);
                }
                if(state.getValue(BurnerBlock.LIT))
                    this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BurnerBlock.LIT, false));
            } else {
                HURemain -= emit;
                emitHeat(emit);
                this.setChanged();
                if(!state.getValue(BurnerBlock.LIT))
                    this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BurnerBlock.LIT, true));
                else this.level.sendBlockUpdated(worldPosition, state, state, 3);
            }
        }
    }

    private boolean consumeFuel() {
    	if(this.getBlockState().getValue(BurnerBlock.REDSTONE_LOCKED))return false;
        if (inv.getStackInSlot(0) == null) return false; 
        int time = inv.getStackInSlot(0).getBurnTime(RecipeType.SMELTING);
        if (time <= 0) return false;
        inv.getStackInSlot(0).shrink(1);
        HURemain += time * ModCommonConfig.COMMON.HUPerFuelTick.get()*getEfficiency();//2.4HU/t

        return true;
    }

    protected void emitHeat(float value) {
        BlockEntity receiver = level.getBlockEntity(this.getBlockPos().above());
        if (receiver instanceof IHeatReceiver) {
            ((IHeatReceiver) receiver).commitHeat(value);
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(componentSpacing.plainCopy().append(Component.translatable("tooltip.steam_and_heat.burner.hu", HURemain).withStyle(ChatFormatting.GOLD)));
        if(!inv.getStackInSlot(0).isEmpty())
        tooltip.add(componentSpacing.plainCopy().append(Component.translatable("tooltip.steam_and_heat.burner.item", inv.getStackInSlot(0).getCount(), inv.getStackInSlot(0).getItem().getName(inv.getStackInSlot(0))).withStyle(ChatFormatting.GRAY)));
        return true;
    }

    /*
     * HU per tick max, 10HU=1 steam
     * */
    protected abstract int getHuPerTick();
    protected abstract double getEfficiency();

    public static float getBoilerHeat(Level level, BlockPos pos, BlockState state) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof BurnerBlockEntity burner) {
            if (burner.HURemain > 0 || state.getValue(BurnerBlock.LIT)) {
                return 1;
            }
        }
        return BoilerHeater.NO_HEAT;
    }
}
