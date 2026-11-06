package org.nina_iseri.steam_and_heat.content.flywheel;

import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BrassSteamFlywheelBlock extends SteamFlywheelBlock {
    public BrassSteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends SteamFlywheelTileEntity> getBlockEntityType() {
        return ModBlockEntities.BRASS_STEAM_FLYWHEEL.get();
    }
}
