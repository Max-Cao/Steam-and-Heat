package org.nina_iseri.steam_and_heat.content.engine.stirling_engine;

import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlock;
import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlockEntity;
import org.nina_iseri.steam_and_heat.content.heat.HeatHandler;
import org.nina_iseri.steam_and_heat.content.heat.IHeatHandler;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 斯特林引擎方块实体。
 *
 * <p>每个 lazy tick 根据底座热源的状态刷新输出：热源“活跃”时输出黄铜飞轮等级的转速，
 * 否则输出 0。同时会从可提取的热源中抽取少量热量，作为引擎运转的消耗。</p>
 */
public class StirlingEngineBlockEntity extends CommonEngineBlockEntity {

    /** 每个 lazy tick 从热源抽取的热量（HU）。 */
    private static final double HEAT_CONSUMED_PER_UPDATE = 1600.0D;

    public StirlingEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        updateGeneratedRotation();
    }

    /**
     * 读取底座热源状态，并更新引擎输出。
     */
    public void updateGeneratedRotation() {
        if (level == null || level.isClientSide) {
            return;
        }

        BlockPos basePos = CommonEngineBlock.getBaseBlockPos(getBlockState(), worldPosition);
        BlockState baseState = level.getBlockState(basePos);
        BlockEntity baseEntity = level.getBlockEntity(basePos);

        StirlingEngineInteractions.InteractionHandler handler = StirlingEngineInteractions.getHandler(baseState);
        StirlingEngineInteractions.HeatSource heatSource = handler.getHeatSource(baseState, baseEntity, level);

        if (heatSource.isEmpty()) {
            setAppliedRotation(0, 0);
            return;
        }

        // 只有真正可提取热量的热源才需要扣除热量；锅炉等只读热源不会受影响。
        if (heatSource.isActive()) {
            IHeatHandler heat = HeatHandler.of(baseEntity, level);
            if (heat != null) {
                heat.exact(HEAT_CONSUMED_PER_UPDATE, false);
            }
        }

        boolean active = heatSource.isActive();
        float speed = active ? ModCommonConfig.COMMON.brassFlywheelSpeed.get() : 0;
        float capacity = active ? ModCommonConfig.COMMON.brassFlywheelCapacity.get() : 0;
        setAppliedRotation(speed, capacity);
    }

    @Override
    protected Block getFlywheelBlock() {
        // 斯特林引擎固定使用黄铜飞轮。
        return ModBlocks.BRASS_FLYWHEEL.get();
    }
}
