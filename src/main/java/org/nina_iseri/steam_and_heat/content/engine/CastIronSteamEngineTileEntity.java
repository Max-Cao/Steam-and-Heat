package org.nina_iseri.steam_and_heat.content.engine;

import org.nina_iseri.steam_and_heat.ModCommonConfig;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CastIronSteamEngineTileEntity extends SteamEngineTileEntity {
    public CastIronSteamEngineTileEntity(BlockEntityType<? extends SteamEngineTileEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Block getFlywheel() {
        return ModBlocks.CAST_IRON_FLYWHEEL.get();
    }

    @Override
    public float getGeneratingCapacity() {
        return ModCommonConfig.COMMON.castIronFlywheelCapacity.get();
    }

    @Override
    public float getGeneratingSpeed() {
        return ModCommonConfig.COMMON.castIronFlywheelSpeed.get();
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return ModCommonConfig.COMMON.castIronFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public int getSteamStorage() {
        return ModCommonConfig.COMMON.castIronFlywheelSteamStorage.get();
    }

    @Override
    public double getSuckEfficiency() {
        return ModCommonConfig.COMMON.castIronFlywheelSuckEfficiency.get();
    }
}
