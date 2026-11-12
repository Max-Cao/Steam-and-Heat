package org.nina_iseri.steam_and_heat.content.engine.steam_engine.bronze;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.content.engine.steam_engine.SteamEngineBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BronzeSteamEngineBlock extends SteamEngineBlock implements IBE<BronzeSteamEngineBlockEntity> {

    public static final MapCodec<BronzeSteamEngineBlock> CODEC = simpleCodec(BronzeSteamEngineBlock::new);

    public BronzeSteamEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends BronzeSteamEngineBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BRONZE_STEAM_ENGINE.get();
    }

    @Override
    public Class<BronzeSteamEngineBlockEntity> getBlockEntityClass() {
        return BronzeSteamEngineBlockEntity.class;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
