package org.nina_iseri.steam_and_heat.content.engine;

import org.nina_iseri.steam_and_heat.SPConfig;
import org.nina_iseri.steam_and_heat.registrate.SPBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BronzeSteamEngineTileEntity extends SteamEngineTileEntity {
    public BronzeSteamEngineTileEntity(BlockEntityType<? extends SteamEngineTileEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Block getFlywheel() {
        return SPBlocks.BRONZE_FLYWHEEL.get();
    }

    @Override
    public float getGeneratingCapacity() {
        return SPConfig.COMMON.bronzeFlywheelCapacity.get();
    }

    @Override
    public float getGeneratingSpeed() {
        return SPConfig.COMMON.bronzeFlywheelSpeed.get();
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return SPConfig.COMMON.bronzeFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public int getSteamStorage() {
        return SPConfig.COMMON.bronzeFlywheelSteamStorage.get();
    }

    @Override
    public double getSuckEfficiency() {
        return SPConfig.COMMON.bronzeFlywheelSuckEfficiency.get();
    }
}
