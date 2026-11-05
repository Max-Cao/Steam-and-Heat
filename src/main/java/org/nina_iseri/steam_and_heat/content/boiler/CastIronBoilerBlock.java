package org.nina_iseri.steam_and_heat.content.boiler;

import com.simibubi.create.foundation.block.IBE;
import org.nina_iseri.steam_and_heat.SPConfig;
import org.nina_iseri.steam_and_heat.registrate.SPBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CastIronBoilerBlock extends BoilerBlock implements IBE<CastIronBoilerBlockEntity> {
    public CastIronBoilerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuConsume() {
		return SPConfig.COMMON.castIronBoilerHU.get();
	}

    @Override
    public Class<CastIronBoilerBlockEntity> getBlockEntityClass() {
        return CastIronBoilerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CastIronBoilerBlockEntity> getBlockEntityType() {
        return SPBlockEntities.CAST_IRON_BOILER.get();
    }
}
