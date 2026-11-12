package org.nina_iseri.steam_and_heat.content.burner.steel;

import org.nina_iseri.steam_and_heat.content.burner.BurnerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteelBurnerBlockEntity extends BurnerBlockEntity {

    public SteelBurnerBlockEntity(BlockEntityType<? extends BurnerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
