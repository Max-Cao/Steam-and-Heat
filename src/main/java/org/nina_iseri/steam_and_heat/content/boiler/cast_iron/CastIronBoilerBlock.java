package org.nina_iseri.steam_and_heat.content.boiler.cast_iron;

import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class CastIronBoilerBlock extends BoilerBlock implements IBE<CastIronBoilerBlockEntity> {
    public CastIronBoilerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuConsume() {
		return ModCommonConfig.COMMON.castIronBoilerHU.get();
	}

    @Override
    public Class<CastIronBoilerBlockEntity> getBlockEntityClass() {
        return CastIronBoilerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CastIronBoilerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.CAST_IRON_BOILER.get();
    }
}
