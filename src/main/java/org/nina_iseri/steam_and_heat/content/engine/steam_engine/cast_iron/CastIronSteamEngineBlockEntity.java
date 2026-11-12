package org.nina_iseri.steam_and_heat.content.engine.steam_engine.cast_iron;

import org.nina_iseri.steam_and_heat.content.engine.steam_engine.SteamEngineBlockEntity;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CastIronSteamEngineBlockEntity extends SteamEngineBlockEntity {

    public CastIronSteamEngineBlockEntity(BlockEntityType<? extends SteamEngineBlockEntity> type, BlockPos pos,
            BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected Block getFlywheelBlock() {
        return ModBlocks.CAST_IRON_FLYWHEEL.get();
    }

    @Override
    public int getSteamStorage() {
        return 64000;
    }
}
