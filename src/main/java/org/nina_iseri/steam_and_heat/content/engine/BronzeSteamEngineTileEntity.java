package org.nina_iseri.steam_and_heat.content.engine;

import org.nina_iseri.steam_and_heat.ModCommonConfig;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

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
        return ModBlocks.BRONZE_FLYWHEEL.get();
    }

    @Override
    public float getGeneratingCapacity() {
        return ModCommonConfig.COMMON.bronzeFlywheelCapacity.get();
    }

    @Override
    public float getGeneratingSpeed() {
        return ModCommonConfig.COMMON.bronzeFlywheelSpeed.get();
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return ModCommonConfig.COMMON.bronzeFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public int getSteamStorage() {
        return ModCommonConfig.COMMON.bronzeFlywheelSteamStorage.get();
    }

    @Override
    public double getSuckEfficiency() {
        return ModCommonConfig.COMMON.bronzeFlywheelSuckEfficiency.get();
    }
}
