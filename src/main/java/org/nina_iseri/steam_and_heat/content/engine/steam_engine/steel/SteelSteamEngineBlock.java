package org.nina_iseri.steam_and_heat.content.engine.steam_engine.steel;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.content.engine.steam_engine.SteamEngineBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteelSteamEngineBlock extends SteamEngineBlock implements IBE<SteelSteamEngineBlockEntity> {

    public static final MapCodec<SteelSteamEngineBlock> CODEC = simpleCodec(SteelSteamEngineBlock::new);

    public SteelSteamEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSteamConsumptionPerTick() {
        return ModCommonConfig.COMMON.steelFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public BlockEntityType<? extends SteelSteamEngineBlockEntity> getBlockEntityType() {
        return ModBlockEntities.STEEL_STEAM_ENGINE.get();
    }

    @Override
    public Class<SteelSteamEngineBlockEntity> getBlockEntityClass() {
        return SteelSteamEngineBlockEntity.class;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
