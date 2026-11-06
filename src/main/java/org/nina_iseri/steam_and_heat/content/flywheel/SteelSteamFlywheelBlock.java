package org.nina_iseri.steam_and_heat.content.flywheel;

import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteelSteamFlywheelBlock extends SteamFlywheelBlock {
    public SteelSteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    //@Override
    public BlockEntityType<? extends SteamFlywheelTileEntity> getBlockEntityType() {
        return ModBlockEntities.STEEL_STEAM_FLYWHEEL.get();
    }
}
