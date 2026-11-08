package org.nina_iseri.steam_and_heat.client;

import org.nina_iseri.steam_and_heat.block.SPBlockPartials;
import org.nina_iseri.steam_and_heat.network.ponder.SPPonderPlugin;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class SteamAndHeatClient {
    public static void addClientListeners(IEventBus forgeEventBus, IEventBus modEventBus) {
        SPBlockPartials.clientInit();
        modEventBus.addListener(SteamAndHeatClient::clientInit);
        modEventBus.addListener(SteamAndHeatClient::registerParticleFactories);
    }

    public static void clientInit(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new SPPonderPlugin());
    }
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.STEAM.get(), SteamParticle.Factory::new);
    }
}
