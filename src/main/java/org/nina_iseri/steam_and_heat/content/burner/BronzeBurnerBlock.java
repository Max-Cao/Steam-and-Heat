package org.nina_iseri.steam_and_heat.content.burner;

import com.simibubi.create.foundation.block.IBE;
import org.nina_iseri.steam_and_heat.SPConfig;
import org.nina_iseri.steam_and_heat.registrate.SPBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BronzeBurnerBlock extends BurnerBlock implements IBE<BronzeBurnerBlockEntity> {
    public BronzeBurnerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuProduce() {
		return SPConfig.COMMON.bronzeBurnerHU.get();
	}

	@Override
	public double getEfficiency() {
		return SPConfig.COMMON.bronzeBurnerEfficiency.get();
	}

	@Override
	public Class<BronzeBurnerBlockEntity> getBlockEntityClass() {
		return BronzeBurnerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends BronzeBurnerBlockEntity> getBlockEntityType() {
		return SPBlockEntities.BRONZE_BURNER.get();
	}
}
