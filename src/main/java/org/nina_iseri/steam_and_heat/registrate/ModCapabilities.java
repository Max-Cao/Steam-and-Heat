package org.nina_iseri.steam_and_heat.registrate;

import org.jetbrains.annotations.Nullable;
import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.content.heat.IHeatHandler;

import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

public class ModCapabilities {
    public static final class Heat {
        public static final BlockCapability<IHeatHandler, @Nullable Void> BLOCK = BlockCapability
                .createVoid(SteamAndHeat.resource("heat_handler"), IHeatHandler.class);
        public static final EntityCapability<IHeatHandler, @Nullable Void> ENTITY = EntityCapability
                .createVoid(SteamAndHeat.resource("heat_handler"), IHeatHandler.class);
        public static final ItemCapability<IHeatHandler, @Nullable Void> ITEM = ItemCapability
                .createVoid(SteamAndHeat.resource("heat_handler"), IHeatHandler.class);
    }
}
