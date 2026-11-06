package org.nina_iseri.steam_and_heat.content.burner;

import com.simibubi.create.foundation.block.IBE;
import org.nina_iseri.steam_and_heat.ModCommonConfig;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteelBurnerBlock extends BurnerBlock implements IBE<SteelBurnerBlockEntity> {
    public SteelBurnerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuProduce() {
		return ModCommonConfig.COMMON.steelBurnerHU.get();
	}

	@Override
	public double getEfficiency() {
		return ModCommonConfig.COMMON.steelBurnerEfficiency.get();
	}

	@Override
	public Class<SteelBurnerBlockEntity> getBlockEntityClass() {
		return SteelBurnerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SteelBurnerBlockEntity> getBlockEntityType() {
		return ModBlockEntities.STEEL_BURNER.get();
	}
}
