package org.nina_iseri.steam_and_heat.content.boiler.steel;

import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteelBoilerBlock extends BoilerBlock implements IBE<SteelBoilerBlockEntity> {
    public SteelBoilerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuConsume() {
		return ModCommonConfig.COMMON.steelBoilerHU.get();
	}

    @Override
    public Class<SteelBoilerBlockEntity> getBlockEntityClass() {
        return SteelBoilerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SteelBoilerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.STEEL_BOILER.get();
    }
}
