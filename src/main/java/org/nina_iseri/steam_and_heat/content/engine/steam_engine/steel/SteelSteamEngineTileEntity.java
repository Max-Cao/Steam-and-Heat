package org.nina_iseri.steam_and_heat.content.engine.steam_engine.steel;

import org.nina_iseri.steam_and_heat.content.engine.steam_engine.SteamEngineTileEntity;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteelSteamEngineTileEntity extends SteamEngineTileEntity {
    public SteelSteamEngineTileEntity(BlockEntityType<? extends SteamEngineTileEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Block getFlywheel() {
        return ModBlocks.STEEL_FLYWHEEL.get();
    }

    @Override
    public float getGeneratingCapacity() {
        return ModCommonConfig.COMMON.steelFlywheelCapacity.get();
    }

    @Override
    public float getGeneratingSpeed() {
        return ModCommonConfig.COMMON.steelFlywheelSpeed.get();
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return ModCommonConfig.COMMON.steelFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public int getSteamStorage() {
        return ModCommonConfig.COMMON.steelFlywheelSteamStorage.get();
    }

    @Override
    public double getSuckEfficiency() {
        return ModCommonConfig.COMMON.steelFlywheelSuckEfficiency.get();
    }
}
