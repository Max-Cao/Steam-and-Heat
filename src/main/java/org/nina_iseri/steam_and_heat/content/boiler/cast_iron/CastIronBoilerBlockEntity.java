package org.nina_iseri.steam_and_heat.content.boiler.cast_iron;

import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CastIronBoilerBlockEntity extends BoilerBlockEntity {

    public CastIronBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
