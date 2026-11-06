package org.nina_iseri.steam_and_heat.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static org.nina_iseri.steam_and_heat.SteamAndHeat.REGISTRATE;

public class ModItems {

    public static final ItemEntry<Item> BRONZE_SHEET =
            REGISTRATE.item("bronze_sheet", Item::new)
                        .register();

    public static final ItemEntry<Item> PRESSURIZED_GAS_CONTAINER =
            REGISTRATE.item("pressurized_gas_container", Item::new)
                        .register();

    public static final ItemEntry<Item> PRESSURIZED_STEAM_CONTAINER =
            REGISTRATE.item("pressurized_steam_container", Item::new)
                        .register();

    public static void register() {
    }
}
