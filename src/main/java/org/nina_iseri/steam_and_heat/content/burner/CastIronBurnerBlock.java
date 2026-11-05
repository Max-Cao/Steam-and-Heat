package org.nina_iseri.steam_and_heat.content.burner;

import com.simibubi.create.foundation.block.IBE;
import org.nina_iseri.steam_and_heat.SPConfig;
import org.nina_iseri.steam_and_heat.registrate.SPBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CastIronBurnerBlock extends BurnerBlock implements IBE<CastIronBurnerBlockEntity> {
    public CastIronBurnerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuProduce() {
		return SPConfig.COMMON.castIronBurnerHU.get();
	}

	@Override
	public double getEfficiency() {
		return SPConfig.COMMON.castIronBurnerEfficiency.get();
	}

	@Override
	public Class<CastIronBurnerBlockEntity> getBlockEntityClass() {
		return CastIronBurnerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CastIronBurnerBlockEntity> getBlockEntityType() {
		return SPBlockEntities.CAST_IRON_BURNER.get();
	}
}
