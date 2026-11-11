package org.nina_iseri.steam_and_heat.content.flywheel;

import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BronzeSteamFlywheelBlock extends SteamFlywheelBlock {
    public BronzeSteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends SteamFlywheelBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BRONZE_STEAM_FLYWHEEL.get();
    }
}
