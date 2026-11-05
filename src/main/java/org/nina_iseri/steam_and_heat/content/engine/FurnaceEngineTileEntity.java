package org.nina_iseri.steam_and_heat.content.engine;

import org.nina_iseri.steam_and_heat.SPConfig;
import org.nina_iseri.steam_and_heat.oldcreatestuff.*;
import org.nina_iseri.steam_and_heat.registrate.SPBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceEngineTileEntity extends OldEngineBlockEntity {

    public FurnaceEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void lazyTick() {
        updateFurnace();
        super.lazyTick();
    }

    public void updateFurnace() {
        BlockState state = level.getBlockState(OldEngineBlock.getBaseBlockPos(getBlockState(), worldPosition));
        FurnaceEngineInteractions.InteractionHandler handler = FurnaceEngineInteractions.getHandler(state);
        FurnaceEngineInteractions.HeatSource heatSource = handler.getHeatSource(state);
        if (heatSource.isEmpty())
            return;

        float modifier = handler.getSpeedModifier(state);
        boolean active = heatSource.isActive();
        float speed = active ? SPConfig.COMMON.brassFlywheelSpeed.get() * modifier : 0;
        float capacity =
                (float) (active ? SPConfig.COMMON.brassFlywheelCapacity.get()
                        : 0);

        appliedCapacity = capacity;
        appliedSpeed = speed;
        refreshWheelSpeed();
    }

    @Override
    public void attachWheel() {
        Direction engineFacing = getBlockState().getValue(OldEngineBlock.FACING);
        BlockPos wheelPos = worldPosition.relative(engineFacing, 2);
        BlockState wheelState = level.getBlockState(wheelPos);
        if (!SPBlocks.BRASS_FLYWHEEL.has(wheelState))
            return;
        Direction wheelFacing = wheelState.getValue(OldFlywheelBlock.HORIZONTAL_FACING);
        if (wheelFacing.getAxis() != engineFacing.getClockWise().getAxis())
            return;
        if (OldFlywheelBlock.isConnected(wheelState)
                && OldFlywheelBlock.getConnection(wheelState) != engineFacing.getOpposite())
            return;
        BlockEntity te = level.getBlockEntity(wheelPos);
        if (te.isRemoved())
            return;
        if (te instanceof OldFlywheelBlockEntity) {
            if (!OldFlywheelBlock.isConnected(wheelState))
                OldFlywheelBlock.setConnection(level, te.getBlockPos(), te.getBlockState(), engineFacing.getOpposite());
            poweredWheel = (OldFlywheelBlockEntity) te;
            refreshWheelSpeed();
        }
    }

}
