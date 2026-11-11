package org.nina_iseri.steam_and_heat.content.engine.stirling_engine;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.nina_iseri.steam_and_heat.content.heat.HeatHandler;
import org.nina_iseri.steam_and_heat.content.heat.IHeatHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 斯特林引擎与“热源方块”之间的适配层。
 *
 * <p>默认情况下，只要方块实体提供可提取的热能能力（{@link IHeatHandler}），它就可以作为热源。
 * 特殊方块可以通过 {@link #registerHandler(Block, InteractionHandler)} 覆盖：
 * 例如高炉会提供 2 倍的转速。</p>
 *
 * <p>示例：</p>
 * <pre>{@code
 * StirlingEngineInteractions.registerHandler(Blocks.REDSTONE_LAMP,
 *     StirlingEngineInteractions.InteractionHandler.of(
 *         state -> state.getValue(RedstoneLampBlock.LIT) ? HeatSource.ACTIVE : HeatSource.VALID,
 *         state -> 1.5f));
 * }</pre>
 */
public final class StirlingEngineInteractions {

    private static final Map<Block, InteractionHandler> HANDLERS = new HashMap<>();
    private static final InteractionHandler DEFAULT_HANDLER = new InteractionHandler() {
    };

    static {
        // 在类加载时注册内置规则，避免忘记在模组初始化阶段调用。
        registerDefaults();
    }

    private StirlingEngineInteractions() {
    }

    public static void registerHandler(Block block, InteractionHandler handler) {
        HANDLERS.put(block, handler);
    }

    public static InteractionHandler getHandler(Block block) {
        return HANDLERS.getOrDefault(block, DEFAULT_HANDLER);
    }

    public static InteractionHandler getHandler(BlockState state) {
        return getHandler(state.getBlock());
    }

    /**
     * 注册内置的热源规则。
     */
    public static void registerDefaults() {
        // 高炉比普通熔炉热得多，因此提供 2 倍转速。
        registerHandler(Blocks.BLAST_FURNACE, InteractionHandler.ofCustomSpeedModifier(state -> 2f));
    }

    /**
     * 根据方块实体上的热能能力推断热源状态。
     */
    static HeatSource getDefaultHeatSource(BlockEntity entity, Level level) {
        IHeatHandler heat = HeatHandler.of(entity, level);
        if (heat == null) {
            return HeatSource.EMPTY;
        }
        if (heat.canExtract() && heat.getHeatAmount() > 0) {
            return HeatSource.ACTIVE;
        }
        // 方块具有热能能力但当前不能提取热量（例如未点燃的熔炉）：允许放置引擎，但不会输出动力。
        return HeatSource.VALID;
    }

    /**
     * 单个热源方块的适配规则。
     */
    public interface InteractionHandler {

        /**
         * 判断该方块当前的热源状态。
         *
         * @param state  热源方块的方块状态
         * @param entity 热源方块的方块实体（可能为 {@code null}）
         * @param level  当前世界
         */
        default HeatSource getHeatSource(BlockState state, BlockEntity entity, Level level) {
            return getDefaultHeatSource(entity, level);
        }

        /**
         * 转速倍率。
         */
        default float getSpeedModifier(BlockState state) {
            return 1f;
        }

        /**
         * 自定义热源状态与转速倍率。
         */
        static InteractionHandler of(Function<BlockState, HeatSource> heatSource,
                Function<BlockState, Float> speedModifier) {
            return new InteractionHandler() {
                @Override
                public HeatSource getHeatSource(BlockState state, BlockEntity entity, Level level) {
                    return heatSource.apply(state);
                }

                @Override
                public float getSpeedModifier(BlockState state) {
                    return speedModifier.apply(state);
                }
            };
        }

        /**
         * 只自定义转速倍率，热源状态仍使用默认的热能能力判断。
         */
        static InteractionHandler ofCustomSpeedModifier(Function<BlockState, Float> speedModifier) {
            return new InteractionHandler() {
                @Override
                public float getSpeedModifier(BlockState state) {
                    return speedModifier.apply(state);
                }
            };
        }
    }

    /**
     * 热源状态。
     */
    public enum HeatSource {
        /** 不是热源。 */
        EMPTY,
        /** 是热源，但当前不能输出热量（例如未点燃的熔炉）。 */
        VALID,
        /** 是热源，且当前可以输出热量。 */
        ACTIVE;

        public boolean isEmpty() {
            return this == EMPTY;
        }

        public boolean isValid() {
            return this != EMPTY;
        }

        public boolean isActive() {
            return this == ACTIVE;
        }
    }
}
