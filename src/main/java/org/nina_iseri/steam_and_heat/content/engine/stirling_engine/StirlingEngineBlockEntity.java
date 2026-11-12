package org.nina_iseri.steam_and_heat.content.engine.stirling_engine;

import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlock;
import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlockEntity;
import org.nina_iseri.steam_and_heat.content.heat.HeatHandler;
import org.nina_iseri.steam_and_heat.content.heat.IHeatHandler;
import org.nina_iseri.steam_and_heat.content.recipe.EngineRecipe;
import org.nina_iseri.steam_and_heat.content.recipe.ModRecipes;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 斯特林引擎方块实体。
 *
 * <p>输入 HU、输出 RPM/SU 全部来自 {@link EngineRecipe}。每个 lazy tick 会一次性
 * 扣除这段时间需要的 HU，因此配方里的 {@code input_per_tick} 仍然是“每 tick”的语义。</p>
 */
public class StirlingEngineBlockEntity extends CommonEngineBlockEntity {

    private EngineRecipe recipe;

    public StirlingEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        updateGeneratedRotation();
    }

    private EngineRecipe getRecipe() {
        if (recipe == null && level != null) {
            recipe = ModRecipes.findEngine(level, new ItemStack(getBlockState().getBlock())).orElse(null);
        }
        return recipe;
    }

    /**
     * 读取底座热源状态，并更新引擎输出。
     */
    public void updateGeneratedRotation() {
        if (level == null || level.isClientSide) {
            return;
        }

        EngineRecipe recipe = getRecipe();
        if (recipe == null || recipe.input() != EngineRecipe.InputType.HU) {
            setAppliedRotation(0, 0);
            return;
        }

        BlockPos basePos = CommonEngineBlock.getBaseBlockPos(getBlockState(), worldPosition);
        BlockState baseState = level.getBlockState(basePos);
        BlockEntity baseEntity = level.getBlockEntity(basePos);

        StirlingEngineInteractions.InteractionHandler handler = StirlingEngineInteractions.getHandler(baseState);
        StirlingEngineInteractions.HeatSource heatSource = handler.getHeatSource(baseState, baseEntity, level);

        if (!heatSource.isActive()) {
            setAppliedRotation(0, 0);
            return;
        }

        // lazyTick 每 lazyTickRate tick 执行一次，所以一次需要扣除这段时间的热量。
        int ticks = Math.max(1, lazyTickRate);
        double needed = (double) recipe.inputPerTick() * ticks;
        IHeatHandler heat = HeatHandler.of(baseEntity, level);
        if (heat != null) {
            if (heat.exact(needed, true) < needed) {
                setAppliedRotation(0, 0);
                return;
            }
            heat.exact(needed, false);
        }

        float modifier = handler.getSpeedModifier(baseState);
        setAppliedRotation(recipe.rpm() * modifier, recipe.su());
    }

    @Override
    protected Block getFlywheelBlock() {
        // 斯特林引擎固定使用黄铜飞轮。
        return ModBlocks.BRASS_FLYWHEEL.get();
    }
}
