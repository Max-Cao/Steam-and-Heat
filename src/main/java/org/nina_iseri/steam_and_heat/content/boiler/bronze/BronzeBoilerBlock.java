package org.nina_iseri.steam_and_heat.content.boiler.bronze;

import com.simibubi.create.foundation.block.IBE;

import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class BronzeBoilerBlock extends BoilerBlock implements IBE<BronzeBoilerBlockEntity> {

    public BronzeBoilerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<BronzeBoilerBlockEntity> getBlockEntityClass() {
        return BronzeBoilerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BronzeBoilerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BRONZE_BOILER.get();
    }
}
