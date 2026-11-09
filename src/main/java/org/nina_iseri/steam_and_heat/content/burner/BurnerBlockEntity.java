package org.nina_iseri.steam_and_heat.content.burner;

import java.util.List;

import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.content.heat.HeatHandler;
import org.nina_iseri.steam_and_heat.content.heat.IHeatHandler;
import org.nina_iseri.steam_and_heat.registrate.ModCapabilities;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

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
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;

public abstract class BurnerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    private ItemStackHandler fuel_handler = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getBurnTime(RecipeType.SMELTING) != 0 && stack.getCraftingRemainingItem().isEmpty();
        }

    };
    // int remain_hu;
    private HeatHandler heat = new HeatHandler(320000, 0, 16000);

    public static final ICapabilityProvider<BurnerBlockEntity, Direction, IItemHandler> ITEM_CAP = (
            BurnerBlockEntity te, Direction side) -> {
        return te.fuel_handler;
    };

    
    public static final ICapabilityProvider<BurnerBlockEntity, Void, IHeatHandler> HEAT_CAP = (
            BurnerBlockEntity te, Void side) -> {
        return te.heat;
    };

    public BurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        fuel_handler.deserializeNBT(registries, nbt.getCompound("inv"));
        // remain_hu = nbt.getInt("hu");
        heat.deserializeNBT(registries, nbt.get("heat"));
        super.read(nbt, registries, clientPacket);
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        nbt.put("inv", fuel_handler.serializeNBT(registries));
        // nbt.putInt("hu", remain_hu);
        nbt.put("heat", heat.serializeNBT(registries));
        super.write(nbt, registries, clientPacket);
    }

    public void tick() {
        if (level == null) {
            return;
        }
        if (!level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            int emit = getHuPerTick();
            // while (remain_hu < emit && this.consumeFuel()) {
            // }
            while (heat.getHeatAmount() < emit && consumeFuel()) {}
            boolean is_lit_before = state.getValue(BurnerBlock.LIT);
            // if (remain_hu == 0) {
            if (heat.noHeat()) {
                if (is_lit_before) {
                    this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BurnerBlock.LIT, false));
                }
                return;
            }
            // int send_out = Math.min(remain_hu, emit);
            double send_out = Math.min(heat.getHeatAmount(), emit);
            produceHeat(send_out);
            // remain_hu -= send_out;
            heat.exact(send_out, false);
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
        if (fuel_handler.getStackInSlot(0) == null)
            return false;
        int time = fuel_handler.getStackInSlot(0).getBurnTime(RecipeType.SMELTING);
        if (time <= 0)
            return false;
        fuel_handler.getStackInSlot(0).shrink(1);
        // remain_hu += time * ModCommonConfig.COMMON.HUPerFuelTick.get() * getEfficiency();// 2.4HU/t
        double heat_get = time * ModCommonConfig.COMMON.HUPerFuelTick.get() * getEfficiency();
        heat.receiveInner(heat_get, false);// 2.4HU/t
        return true;
    }

    protected void produceHeat(double value) {
        BlockPos above = this.getBlockPos().above();
        // if (above instanceof IAttachHeat receiver) {
        //     receiver.receiveHeat(value);
        // }
        BlockState aboveState = level.getBlockState(above);
        IHeatHandler aboveHeatHandler = level.getCapability(ModCapabilities.Heat.BLOCK, above, aboveState, level.getBlockEntity(above));
        if (aboveHeatHandler != null) {
            aboveHeatHandler.receive(value, false);
        }
        // if ()
    }

    // @Override
    // public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
    //     tooltip.add(componentSpacing.plainCopy().append(
    //             new LangBuilder(SteamAndHeat.MODID).translate("tooltip.burner.hu", hu_remain).style(ChatFormatting.GOLD)));
    //     if (!fuel.getStackInSlot(0).isEmpty())
    //         tooltip.add(componentSpacing.plainCopy()
    //                 .append(Component
    //                         .translatable("tooltip.burner.item", fuel.getStackInSlot(0).getCount(),
    //                                 fuel.getStackInSlot(0).getItem().getName(fuel.getStackInSlot(0)))
    //                         .style(ChatFormatting.GRAY)));
    //     return true;
    // }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		tooltip.clear();
        // new LangBuilder(SteamAndHeat.MODID).translate("tooltip.burner.hu", remain_hu)
        new LangBuilder(SteamAndHeat.MODID).translate("tooltip.burner.hu", heat.getHeatAmount())
                .style(ChatFormatting.GOLD)
                .forGoggles(tooltip, 1);
        ItemStack fuel = fuel_handler.getStackInSlot(0);
        if (!fuel.isEmpty()) {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.burner.item",
                    fuel.getCount(),
                    fuel.getItem().getName(fuel))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
        }
        return true;
    }

    /*
     * HU per tick max, 10HU=1 steam
     */
    protected abstract int getHuPerTick();

    protected abstract double getEfficiency();

    public static double getBoilerHeat(Level level, BlockPos pos, BlockState state) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof BurnerBlockEntity burner) {
            // if (burner.remain_hu > 0 || state.getValue(BurnerBlock.LIT)) {
            if (burner.heat.getHeatAmount() > 0 || state.getValue(BurnerBlock.LIT)) {
                return 1;
            }
        }
        return BoilerHeater.NO_HEAT;
    }
}
