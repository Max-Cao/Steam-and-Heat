package org.nina_iseri.steam_and_heat.content.burner.bronze;

import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.content.burner.BurnerBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class BronzeBurnerBlock extends BurnerBlock implements IBE<BronzeBurnerBlockEntity> {

    public BronzeBurnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<BronzeBurnerBlockEntity> getBlockEntityClass() {
        return BronzeBurnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BronzeBurnerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BRONZE_SOLID_BURNER.get();
    }
}
