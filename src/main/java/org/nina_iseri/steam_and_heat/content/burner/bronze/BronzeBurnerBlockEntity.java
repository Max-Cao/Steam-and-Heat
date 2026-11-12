package org.nina_iseri.steam_and_heat.content.burner.bronze;

import org.nina_iseri.steam_and_heat.content.burner.BurnerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BronzeBurnerBlockEntity extends BurnerBlockEntity {

    public BronzeBurnerBlockEntity(BlockEntityType<? extends BurnerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
