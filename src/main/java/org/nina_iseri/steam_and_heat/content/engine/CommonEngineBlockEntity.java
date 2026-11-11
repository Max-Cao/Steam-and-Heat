package org.nina_iseri.steam_and_heat.content.engine;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.nina_iseri.steam_and_heat.content.flywheel.CommonFlywheelBlock;
import org.nina_iseri.steam_and_heat.content.flywheel.CommonFlywheelBlockEntity;

import java.util.List;

/**
 * 所有引擎方块实体的公共父类。
 *
 * <p>这里集中处理“把引擎产生的转速/应力传递给飞轮”这件事，子类只需要决定：</p>
 * <ul>
 *     <li>可以使用哪种飞轮（{@link #getFlywheelBlock()}）；</li>
 *     <li>当前应该输出多少转速和应力。</li>
 * </ul>
 *
 * <p>飞轮的连接状态记录在飞轮方块的 {@link CommonFlywheelBlock#CONNECTION} 属性中，
 * 引擎本身不保存连接信息，因此区块卸载/重新加载后可以自动重新连接。</p>
 */
public abstract class CommonEngineBlockEntity extends SmartBlockEntity {

    /** 当前输出给飞轮的转速。 */
    protected float appliedSpeed;
    /** 当前输出给飞轮的应力容量。 */
    protected float appliedCapacity;

    @Nullable
    protected CommonFlywheelBlockEntity poweredWheel;

    public CommonEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        // 引擎目前不需要额外的 Create BlockEntityBehaviour。
    }

    /**
     * 该类引擎可以驱动的飞轮方块。
     */
    protected abstract Block getFlywheelBlock();

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null || level.isClientSide) {
            return;
        }

        // 飞轮被破坏或区块重新加载后，连接会失效，需要重新寻找。
        if (poweredWheel != null && poweredWheel.isRemoved()) {
            poweredWheel = null;
        }
        if (poweredWheel == null) {
            attachWheel();
        } else {
            refreshWheelSpeed();
        }
    }

    /**
     * 尝试连接引擎正前方两格处的飞轮。
     *
     * <p>连接方向会写入飞轮方块的 {@code CONNECTION} 属性，飞轮据此渲染连接杆。</p>
     */
    public void attachWheel() {
        if (level == null || level.isClientSide) {
            return;
        }

        Direction engineFacing = getBlockState().getValue(CommonEngineBlock.FACING);
        BlockPos wheelPos = worldPosition.relative(engineFacing, 2);
        BlockState wheelState = level.getBlockState(wheelPos);

        // 只接受该引擎对应的飞轮，并且飞轮的轴向必须与引擎朝向垂直。
        if (wheelState.getBlock() != getFlywheelBlock()) {
            return;
        }
        Direction wheelFacing = wheelState.getValue(CommonFlywheelBlock.HORIZONTAL_FACING);
        if (wheelFacing.getAxis() != engineFacing.getClockWise().getAxis()) {
            return;
        }

        // 飞轮已经连接了别的引擎时，不能抢占。
        if (CommonFlywheelBlock.isConnected(wheelState)
                && CommonFlywheelBlock.getConnection(wheelState) != engineFacing.getOpposite()) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(wheelPos);
        if (!(blockEntity instanceof CommonFlywheelBlockEntity flywheel) || blockEntity.isRemoved()) {
            return;
        }

        if (!CommonFlywheelBlock.isConnected(wheelState)) {
            CommonFlywheelBlock.setConnection(level, wheelPos, wheelState, engineFacing.getOpposite());
        }

        poweredWheel = flywheel;
        refreshWheelSpeed();
    }

    /**
     * 断开与飞轮的连接，并让飞轮停止转动。
     */
    public void detachWheel() {
        if (poweredWheel == null) {
            return;
        }

        if (!poweredWheel.isRemoved()) {
            poweredWheel.setRotation(0, 0);
            CommonFlywheelBlock.setConnection(level, poweredWheel.getBlockPos(), poweredWheel.getBlockState(), null);
        }
        poweredWheel = null;
    }

    /**
     * 更新当前输出，并在数值真正变化时同步给飞轮。
     */
    protected void setAppliedRotation(float speed, float capacity) {
        if (appliedSpeed == speed && appliedCapacity == capacity) {
            return;
        }
        appliedSpeed = speed;
        appliedCapacity = capacity;
        refreshWheelSpeed();
    }

    /**
     * 把当前输出写入已连接的飞轮。
     */
    protected void refreshWheelSpeed() {
        if (poweredWheel != null && !poweredWheel.isRemoved()) {
            poweredWheel.setRotation(appliedSpeed, appliedCapacity);
        }
    }

    @Override
    public void remove() {
        super.remove();
        // 方块被破坏时断开连接；区块卸载不会走到这里（由 Create 的 SmartBlockEntity 处理）。
        detachWheel();
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(1.5);
    }
}
