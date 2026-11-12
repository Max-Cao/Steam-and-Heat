package org.nina_iseri.steam_and_heat.content.burner.cast_iron;

import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.content.burner.BurnerBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class CastIronBurnerBlock extends BurnerBlock implements IBE<CastIronBurnerBlockEntity> {

    public CastIronBurnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<CastIronBurnerBlockEntity> getBlockEntityClass() {
        return CastIronBurnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CastIronBurnerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.CAST_IRON_SOLID_BURNER.get();
    }
}
