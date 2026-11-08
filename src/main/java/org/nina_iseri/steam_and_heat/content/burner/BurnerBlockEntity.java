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
    private ItemStackHandler fuel = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getBurnTime(RecipeType.SMELTING) != 0 && stack.getCraftingRemainingItem().isEmpty();
        }

    };
    int hu_remain;

    public static final ICapabilityProvider<BurnerBlockEntity, Direction, IItemHandler> ITEM_CAP = (
            BurnerBlockEntity te, Direction side) -> {
        return te.fuel;
    };

    public BurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        fuel.deserializeNBT(registries, nbt.getCompound("inv"));
        hu_remain = nbt.getInt("hu");
        super.read(nbt, registries, clientPacket);
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        nbt.put("inv", fuel.serializeNBT(registries));
        nbt.putInt("hu", hu_remain);
        super.write(nbt, registries, clientPacket);
    }

    public void tick() {
        if (level == null) {
            return;
        }
        if (!level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            int emit = getHuPerTick();
            while (hu_remain < emit && this.consumeFuel()) {
            }
            boolean is_lit_before = state.getValue(BurnerBlock.LIT);
            if (hu_remain == 0) {
                if (is_lit_before) {
                    this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BurnerBlock.LIT, false));
                }
                return;
            }
            int send_out = Math.min(hu_remain, emit);
            produceHeat(send_out);
            hu_remain -= send_out;
            this.setChanged();
            if (!is_lit_before) {
                this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BurnerBlock.LIT, true));
            } else {
                this.level.sendBlockUpdated(worldPosition, state, state, 3);
            }
        }
    }

    private boolean consumeFuel() {
        if (this.getBlockState().getValue(BurnerBlock.REDSTONE_LOCKED))
            return false;
        if (fuel.getStackInSlot(0) == null)
            return false;
        int time = fuel.getStackInSlot(0).getBurnTime(RecipeType.SMELTING);
        if (time <= 0)
            return false;
        fuel.getStackInSlot(0).shrink(1);
        hu_remain += time * ModCommonConfig.COMMON.HUPerFuelTick.get() * getEfficiency();// 2.4HU/t

        return true;
    }

    protected void produceHeat(float value) {
        BlockEntity above = level.getBlockEntity(this.getBlockPos().above());
        if (above instanceof IHeatReceiver receiver) {
            receiver.commitHeat(value);
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(componentSpacing.plainCopy().append(
                Component.translatable("tooltip.steam_and_heat.burner.hu", hu_remain).withStyle(ChatFormatting.GOLD)));
        if (!fuel.getStackInSlot(0).isEmpty())
            tooltip.add(componentSpacing.plainCopy()
                    .append(Component
                            .translatable("tooltip.steam_and_heat.burner.item", fuel.getStackInSlot(0).getCount(),
                                    fuel.getStackInSlot(0).getItem().getName(fuel.getStackInSlot(0)))
                            .withStyle(ChatFormatting.GRAY)));
        return true;
    }

    /*
     * HU per tick max, 10HU=1 steam
     */
    protected abstract int getHuPerTick();

    protected abstract double getEfficiency();

    public static float getBoilerHeat(Level level, BlockPos pos, BlockState state) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof BurnerBlockEntity burner) {
            if (burner.hu_remain > 0 || state.getValue(BurnerBlock.LIT)) {
                return 1;
            }
        }
        return BoilerHeater.NO_HEAT;
    }
}
