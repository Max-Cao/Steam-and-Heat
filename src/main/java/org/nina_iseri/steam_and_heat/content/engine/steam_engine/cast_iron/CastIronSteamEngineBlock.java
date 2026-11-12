package org.nina_iseri.steam_and_heat.content.engine.steam_engine.cast_iron;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.content.engine.steam_engine.SteamEngineBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CastIronSteamEngineBlock extends SteamEngineBlock implements IBE<CastIronSteamEngineBlockEntity> {

    public static final MapCodec<CastIronSteamEngineBlock> CODEC = simpleCodec(CastIronSteamEngineBlock::new);

    public CastIronSteamEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends CastIronSteamEngineBlockEntity> getBlockEntityType() {
        return ModBlockEntities.CAST_IRON_STEAM_ENGINE.get();
    }

    @Override
    public Class<CastIronSteamEngineBlockEntity> getBlockEntityClass() {
        return CastIronSteamEngineBlockEntity.class;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
