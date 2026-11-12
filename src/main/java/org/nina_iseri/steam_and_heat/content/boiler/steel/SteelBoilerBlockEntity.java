package org.nina_iseri.steam_and_heat.content.boiler.steel;

import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteelBoilerBlockEntity extends BoilerBlockEntity {

    public SteelBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
