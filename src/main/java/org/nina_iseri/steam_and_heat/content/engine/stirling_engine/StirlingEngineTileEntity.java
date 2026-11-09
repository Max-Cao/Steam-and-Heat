package org.nina_iseri.steam_and_heat.content.engine.stirling_engine;

import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlock;
import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlockEntity;
import org.nina_iseri.steam_and_heat.content.flywheel.CommonFlywheelBlock;
import org.nina_iseri.steam_and_heat.content.flywheel.CommonFlywheelBlockEntity;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StirlingEngineTileEntity extends CommonEngineBlockEntity {

    public StirlingEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void lazyTick() {
        updateFurnace();
        super.lazyTick();
    }

    public void updateFurnace() {
        BlockState state = level.getBlockState(CommonEngineBlock.getBaseBlockPos(getBlockState(), worldPosition));
        StirlingEngineInteractions.InteractionHandler handler = StirlingEngineInteractions.getHandler(state);
        StirlingEngineInteractions.HeatSource heatSource = handler.getHeatSource(state);
        if (heatSource.isEmpty())
            return;

        float modifier = handler.getSpeedModifier(state);
        boolean active = heatSource.isActive();
        float speed = active ? ModCommonConfig.COMMON.brassFlywheelSpeed.get() * modifier : 0;
        float capacity =
                (float) (active ? ModCommonConfig.COMMON.brassFlywheelCapacity.get()
                        : 0);

        appliedCapacity = capacity;
        appliedSpeed = speed;
        refreshWheelSpeed();
    }

    @Override
    public void attachWheel() {
        Direction engineFacing = getBlockState().getValue(CommonEngineBlock.FACING);
        BlockPos wheelPos = worldPosition.relative(engineFacing, 2);
        BlockState wheelState = level.getBlockState(wheelPos);
        if (!ModBlocks.BRASS_FLYWHEEL.has(wheelState))
            return;
        Direction wheelFacing = wheelState.getValue(CommonFlywheelBlock.HORIZONTAL_FACING);
        if (wheelFacing.getAxis() != engineFacing.getClockWise().getAxis())
            return;
        if (CommonFlywheelBlock.isConnected(wheelState)
                && CommonFlywheelBlock.getConnection(wheelState) != engineFacing.getOpposite())
            return;
        BlockEntity te = level.getBlockEntity(wheelPos);
        if (te.isRemoved())
            return;
        if (te instanceof CommonFlywheelBlockEntity) {
            if (!CommonFlywheelBlock.isConnected(wheelState))
                CommonFlywheelBlock.setConnection(level, te.getBlockPos(), te.getBlockState(), engineFacing.getOpposite());
            poweredWheel = (CommonFlywheelBlockEntity) te;
            refreshWheelSpeed();
        }
    }

}
