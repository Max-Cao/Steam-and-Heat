package org.nina_iseri.steam_and_heat.content.engine;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * 所有“引擎”方块的公共父类。
 *
 * <p>引擎本身不存储能量，它只是贴在某个底座方块（热源或锅炉）上，并把产生的旋转动力
 * 输出到正前方两格处的飞轮。{@link #FACING} 始终由底座指向引擎（也就是飞轮的方向），
 * 因此“底座位置”就是 {@code pos.relative(FACING.getOpposite())}。</p>
 */
public abstract class CommonEngineBlock extends HorizontalDirectionalBlock implements IWrenchable {

    protected CommonEngineBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * 根据方块状态计算引擎所依附的底座（热源/锅炉）位置。
     */
    public static BlockPos getBaseBlockPos(BlockState state, BlockPos pos) {
        return pos.relative(state.getValue(FACING).getOpposite());
    }

    /**
     * 原版 {@link Block#canSurvive(BlockState, LevelReader, BlockPos)} 的实现。
     *
     * <p>它会在 {@code BlockItem} 真正放置方块之前被调用，因此这里同时承担两个职责：</p>
     * <ol>
     *     <li>同一个底座方块上最多只能有一台引擎；</li>
     *     <li>底座本身必须能作为该类引擎的热源/支撑方块。</li>
     * </ol>
     *
     * <p>这样做的好处是：不合法的放置会直接被原版取消，不会出现“放下后立刻被破坏”、
     * 物品被消耗或掉落的糟糕体验。通过命令 / 结构方块等绕过 {@code BlockItem} 的放置，
     * 则由 {@link #onPlace}（同一底座冲突）与 {@link #neighborChanged}（底座失效）兜底处理。</p>
     */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // 一个底座只能连接一台引擎。注意这里必须比较“底座坐标”，而不是简单比较相邻方块，
        // 否则两个相邻的热源会互相把对方的引擎误判成自己的第二台引擎。
        if (findOtherEngineOnSameBase(state, level, pos) != null) {
            return false;
        }

        // 能力查询需要 Level；世界生成等只读场景没有能力系统，此时只做结构检查。
        if (level instanceof Level actualLevel) {
            BlockPos basePos = getBaseBlockPos(state, pos);
            return isValidBase(actualLevel.getBlockState(basePos), actualLevel, basePos);
        }
        return true;
    }

    /**
     * 查找同一底座上的另一台引擎。
     *
     * @return 另一台引擎的位置；没有则返回 {@code null}
     */
    @Nullable
    protected BlockPos findOtherEngineOnSameBase(BlockState state, BlockGetter level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos basePos = pos.relative(facing.getOpposite());

        // 只检查水平方向：引擎只会贴在底座的四个水平面上。
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            // 跳过当前引擎自己所在的方向，否则会把自己当成“另一台引擎”。
            if (direction == facing) {
                continue;
            }

            BlockPos candidatePos = basePos.relative(direction);
            BlockState candidateState = level.getBlockState(candidatePos);
            if (!(candidateState.getBlock() instanceof CommonEngineBlock)) {
                continue;
            }

            // 相邻的引擎不一定属于同一个底座（可能分别贴在两个相邻的热源上），
            // 因此还要确认对方的 FACING 确实指向当前底座。
            if (basePos.equals(getBaseBlockPos(candidateState, candidatePos))) {
                return candidatePos;
            }
        }
        return null;
    }

    /**
     * 检查底座方块是否允许该类引擎工作。只有存在 {@link Level} 时才会调用。
     *
     * @param baseState 底座方块的方块状态
     * @param level     当前世界
     * @param basePos   底座方块的位置
     */
    protected abstract boolean isValidBase(BlockState baseState, Level level, BlockPos basePos);

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        // 引擎不提供扳手旋转功能，避免误操作改变朝向导致连接断开。
        return InteractionResult.FAIL;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        // 正常放置已经由 canSurvive 拦截。这里只检查“同一底座上是否已有另一台引擎”，
        // 不检查热源能力：在结构方块/蓝图同一 tick 放置底座与引擎时，底座的方块实体
        // 可能还没有创建，此时查询热源能力会误判。
        // 另外不能在此处直接 destroyBlock：onPlace 执行时方块实体尚未创建，
        // 递归删除方块会导致一个“幽灵”方块实体被创建在空气位置上。
        if (!level.isClientSide && findOtherEngineOnSameBase(state, level, pos) != null) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
            BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        // 只有底座本身发生变化时才需要重新检查热源是否仍然有效。
        // 其它方向方块的变化不会影响引擎，忽略它们可以避免蓝图/结构方块放置过程中的误判。
        if (!level.isClientSide && neighborPos.equals(getBaseBlockPos(state, pos))
                && !canSurvive(state, level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            // 此时方块实体已经创建，可以安全地走正常的破坏流程并掉落引擎物品。
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        // 引擎只能水平放置；如果点击的是顶面/底面，则使用玩家朝向的反方向。
        if (facing.getAxis().isVertical()) {
            facing = context.getHorizontalDirection().getOpposite();
        }
        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.FURNACE_ENGINE.get(state.getValue(FACING));
    }

    /**
     * 引擎框架模型。仅在客户端渲染时使用。
     */
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public abstract PartialModel getFrameModel();
}
