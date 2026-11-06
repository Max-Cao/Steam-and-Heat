package org.nina_iseri.steam_and_heat.registrate;

import static org.nina_iseri.steam_and_heat.SteamAndHeat.REGISTRATE;

import org.nina_iseri.steam_and_heat.SteamAndHeat;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, SteamAndHeat.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.steam_and_heat.base"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(ModBlocks.STEEL_FLYWHEEL::asStack)
                    .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
        REGISTRATE.defaultCreativeTab(MOD_TAB.getKey());
    }
}
