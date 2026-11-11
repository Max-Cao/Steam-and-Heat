package org.nina_iseri.steam_and_heat.content.engine.steam_engine.bronze;

import org.nina_iseri.steam_and_heat.content.engine.steam_engine.SteamEngineBlockEntity;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BronzeSteamEngineBlockEntity extends SteamEngineBlockEntity {
    public BronzeSteamEngineBlockEntity(BlockEntityType<? extends SteamEngineBlockEntity> type, BlockPos pos,
            BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected Block getFlywheelBlock() {
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
