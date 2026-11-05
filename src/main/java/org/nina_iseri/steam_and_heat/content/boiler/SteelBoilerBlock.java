package org.nina_iseri.steam_and_heat.content.boiler;

import com.simibubi.create.foundation.block.IBE;
import org.nina_iseri.steam_and_heat.SPConfig;
import org.nina_iseri.steam_and_heat.registrate.SPBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteelBoilerBlock extends BoilerBlock implements IBE<SteelBoilerBlockEntity> {
    public SteelBoilerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuConsume() {
		return SPConfig.COMMON.steelBoilerHU.get();
	}

    @Override
    public Class<SteelBoilerBlockEntity> getBlockEntityClass() {
        return SteelBoilerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SteelBoilerBlockEntity> getBlockEntityType() {
        return SPBlockEntities.STEEL_BOILER.get();
    }
}
