package org.nina_iseri.steam_and_heat.content.flywheel;

import org.nina_iseri.steam_and_heat.content.oldcreatestuff.OldFlywheelBlock;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteamFlywheelBlock extends OldFlywheelBlock {
    public SteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends SteamFlywheelTileEntity> getBlockEntityType() {
        return null;
    }
}
