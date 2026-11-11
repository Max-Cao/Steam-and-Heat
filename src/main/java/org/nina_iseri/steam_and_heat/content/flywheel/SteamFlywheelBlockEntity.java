package org.nina_iseri.steam_and_heat.content.flywheel;

import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 蒸汽飞轮的方块实体。飞轮本身只负责旋转和发电，连接关系由引擎写入方块状态。
 */
public class SteamFlywheelBlockEntity extends CommonFlywheelBlockEntity {

    public SteamFlywheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null || level.isClientSide) {
            return;
        }

        BlockState state = getBlockState();
        if (!SteamFlywheelBlock.isConnected(state)) {
            return;
        }

        // 连接的引擎被拆除后，飞轮应当自动清除连接状态，避免悬空的连接杆。
        var connection = SteamFlywheelBlock.getConnection(state);
        if (connection == null) {
            return;
        }

        BlockPos enginePos = worldPosition.relative(connection, 2);
        BlockEntity engine = level.getBlockEntity(enginePos);
        if (engine instanceof CommonEngineBlockEntity) {
            return;
        }

        clearState();
    }

    public void clearState() {
        setRotation(0.0F, 0.0F);
        SteamFlywheelBlock.setConnection(level, worldPosition, getBlockState(), null);
    }
}
