package org.nina_iseri.steam_and_heat.content.engine.stirling_engine;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.client.ModBlockPartials;
import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * 斯特林引擎（旧 Create 的熔炉引擎）。
 *
 * <p>它贴在具有热能能力（{@link org.nina_iseri.steam_and_heat.content.heat.IHeatHandler}）
 * 的方块上，根据热源是否处于“可提取热量”状态来产生旋转动力。</p>
 */
@EventBusSubscriber
public class StirlingEngineBlock extends CommonEngineBlock implements IBE<StirlingEngineBlockEntity> {

    public static final MapCodec<StirlingEngineBlock> CODEC = simpleCodec(StirlingEngineBlock::new);

    public StirlingEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isValidBase(BlockState baseState, Level level, BlockPos basePos) {
        // 注意：这里必须用底座的状态来查找 InteractionHandler，而不是引擎自己的状态。
        return StirlingEngineInteractions.getHandler(baseState)
                .getHeatSource(baseState, level.getBlockEntity(basePos), level)
                .isValid();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PartialModel getFrameModel() {
        return ModBlockPartials.FURNACE_GENERATOR_FRAME;
    }

    /**
     * 手持引擎物品右键熔炉时，阻止原版熔炉打开 GUI，让物品能够正常放置。
     */
    @SubscribeEvent
    public static void preventFurnaceGuiOpening(PlayerInteractEvent.RightClickBlock event) {
        Direction face = event.getFace();
        if (face != null && face.getAxis().isVertical()) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return;
        }
        if (blockItem.getBlock() != ModBlocks.FURNACE_ENGINE.get()) {
            return;
        }

        BlockState clickedState = event.getLevel().getBlockState(event.getPos());
        if (clickedState.getBlock() instanceof AbstractFurnaceBlock) {
            event.setUseBlock(TriState.FALSE);
        }
    }

    @Override
    public Class<StirlingEngineBlockEntity> getBlockEntityClass() {
        return StirlingEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StirlingEngineBlockEntity> getBlockEntityType() {
        return ModBlockEntities.FURNACE_ENGINE.get();
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
