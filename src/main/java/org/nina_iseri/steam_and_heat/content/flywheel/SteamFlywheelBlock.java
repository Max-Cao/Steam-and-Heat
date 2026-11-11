package org.nina_iseri.steam_and_heat.content.flywheel;

import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * 通用蒸汽飞轮的抽象基类；具体等级由子类决定使用的方块实体类型。
 */
public abstract class SteamFlywheelBlock extends CommonFlywheelBlock {

    public SteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public abstract BlockEntityType<? extends SteamFlywheelBlockEntity> getBlockEntityType();
}
