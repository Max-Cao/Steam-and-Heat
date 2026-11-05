package org.nina_iseri.steam_and_heat.content.boiler;

import org.nina_iseri.steam_and_heat.SPConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteelBoilerBlockEntity extends BoilerTileEntity {

    public SteelBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected int getHUPerTick() {
        return SPConfig.COMMON.steelBoilerHU.get();
    }

}
