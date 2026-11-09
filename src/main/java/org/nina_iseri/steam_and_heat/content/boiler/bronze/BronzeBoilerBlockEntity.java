package org.nina_iseri.steam_and_heat.content.boiler.bronze;

import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlockEntity;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BronzeBoilerBlockEntity extends BoilerBlockEntity {

    public BronzeBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected double getHUPerTick() {
        return ModCommonConfig.COMMON.bronzeBoilerHU.get();
    }

}
