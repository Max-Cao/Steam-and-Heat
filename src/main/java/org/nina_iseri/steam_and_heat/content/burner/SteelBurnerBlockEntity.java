package org.nina_iseri.steam_and_heat.content.burner;

import org.nina_iseri.steam_and_heat.SPConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteelBurnerBlockEntity extends BurnerBlockEntity {

    public SteelBurnerBlockEntity(BlockEntityType<? extends BurnerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected int getHuPerTick() {
        return SPConfig.COMMON.steelBurnerHU.get();
    }
	@Override
	public double getEfficiency() {
		return SPConfig.COMMON.steelBurnerEfficiency.get();
	}
}
