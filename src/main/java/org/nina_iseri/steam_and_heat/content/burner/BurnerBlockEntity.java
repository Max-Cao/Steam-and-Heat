package org.nina_iseri.steam_and_heat.content.burner;

import java.util.List;
import java.util.Optional;

import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.content.heat.HeatGoggles;
import org.nina_iseri.steam_and_heat.content.heat.HeatHandler;
import org.nina_iseri.steam_and_heat.content.heat.IHeatHandler;
import org.nina_iseri.steam_and_heat.content.recipe.ModRecipes;
import org.nina_iseri.steam_and_heat.content.recipe.SolidBurnerRecipe;
import org.nina_iseri.steam_and_heat.registrate.ModCapabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;

/**
 * 固体燃烧室方块实体。
 *
 * <p>燃料是否可用、每个燃料提供多少 HU、每 tick 输出多少 HU，全部来自
 * {@link SolidBurnerRecipe}。因此新增/修改燃料只需要改数据包配方，不需要改代码或配置。</p>
 */
public abstract class BurnerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    private static final double HEAT_LOSS_PER_TICK = 320.0D;

    private final ItemStackHandler fuel_handler = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            SolidBurnerRecipe recipe = getRecipe();
            return recipe != null && recipe.findFuel(stack).isPresent();
        }
    };

    private final HeatHandler heat = new HeatHandler(320000, 0, 16000);

    private SolidBurnerRecipe recipe;

    public static final ICapabilityProvider<BurnerBlockEntity, Direction, IItemHandler> ITEM_CAP =
            (blockEntity, side) -> blockEntity.fuel_handler;

    public static final ICapabilityProvider<BurnerBlockEntity, Void, IHeatHandler> HEAT_CAP =
            (blockEntity, side) -> blockEntity.heat;

    public BurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    /**
     * 查询并缓存当前燃烧室对应的配方。
     */
    protected SolidBurnerRecipe getRecipe() {
        if (recipe == null && level != null) {
            recipe = ModRecipes.findBurner(level, new ItemStack(getBlockState().getBlock())).orElse(null);
        }
        return recipe;
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        fuel_handler.deserializeNBT(registries, nbt.getCompound("inv"));
        heat.deserializeNBT(registries, nbt.get("heat"));
        super.read(nbt, registries, clientPacket);
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        nbt.put("inv", fuel_handler.serializeNBT(registries));
        nbt.put("heat", heat.serializeNBT(registries));
        super.write(nbt, registries, clientPacket);
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) {
            return;
        }

        SolidBurnerRecipe recipe = getRecipe();
        if (recipe == null) {
            return;
        }

        BlockState state = getBlockState();
        int emit = recipe.huPerTick();

        // 热量不足时消耗燃料，直到可以输出一个 tick 的热量。
        while (heat.getHeatAmount() < emit && consumeFuel(recipe)) {
        }

        boolean wasLit = state.getValue(BurnerBlock.LIT);
        if (heat.noHeat()) {
            if (wasLit) {
                level.setBlockAndUpdate(worldPosition, state.setValue(BurnerBlock.LIT, false));
            }
            return;
        }

        double output = Math.min(heat.getHeatAmount(), emit);
        produceHeat(output);

        setChanged();
        if (!wasLit) {
            level.setBlockAndUpdate(worldPosition, state.setValue(BurnerBlock.LIT, true));
        } else {
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }

    private boolean consumeFuel(SolidBurnerRecipe recipe) {
        if (getBlockState().getValue(BurnerBlock.REDSTONE_LOCKED)) {
            return false;
        }

        ItemStack fuelStack = fuel_handler.getStackInSlot(0);
        if (fuelStack.isEmpty()) {
            return false;
        }

        Optional<SolidBurnerRecipe.Fuel> fuel = recipe.findFuel(fuelStack);
        if (fuel.isEmpty()) {
            return false;
        }

        fuelStack.shrink(1);
        heat.receiveInner(fuel.get().hu(), false);
        return true;
    }

    protected void produceHeat(double value) {
        BlockPos above = getBlockPos().above();
        BlockState aboveState = level.getBlockState(above);
        IHeatHandler aboveHeatHandler = level.getCapability(ModCapabilities.Heat.BLOCK, above, aboveState,
                level.getBlockEntity(above));

        if (aboveHeatHandler != null && aboveHeatHandler.receive(value, false) > 0) {
            heat.exact(value, false);
        }

        // 没有接收方时热量会散失。
        heat.exact(HEAT_LOSS_PER_TICK, false);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.clear();
        HeatGoggles.addToTooltip(tooltip, heat);

        SolidBurnerRecipe recipe = getRecipe();
        if (recipe != null) {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.burner.huproduce", recipe.huPerTick())
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip, 1);
        }

        ItemStack fuel = fuel_handler.getStackInSlot(0);
        if (!fuel.isEmpty()) {
            new LangBuilder(SteamAndHeat.MODID)
                    .translate("tooltip.burner.item", fuel.getCount(), fuel.getItem().getName(fuel))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
        }
        return true;
    }

    public static double getBoilerHeat(Level level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BurnerBlockEntity burner) {
            if (burner.heat.getHeatAmount() > 0 || state.getValue(BurnerBlock.LIT)) {
                return 1;
            }
        }
        return BoilerHeater.NO_HEAT;
    }
}
