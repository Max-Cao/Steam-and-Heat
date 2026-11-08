package org.nina_iseri.steam_and_heat.content.flywheel;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteamFlywheelBlock extends CommonFlywheelBlock {
    public SteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends SteamFlywheelTileEntity> getBlockEntityType() {
        return null;
    }
}
