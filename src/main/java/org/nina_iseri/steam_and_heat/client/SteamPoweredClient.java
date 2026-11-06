package org.nina_iseri.steam_and_heat.client;

import org.nina_iseri.steam_and_heat.block.SPBlockPartials;
import org.nina_iseri.steam_and_heat.network.ponder.SPPonderPlugin;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

public class SteamPoweredClient {
    public static void addClientListeners(IEventBus forgeEventBus, IEventBus modEventBus) {
        SPBlockPartials.clientInit();
        modEventBus.addListener(SteamPoweredClient::clientInit);
        modEventBus.addListener(SteamPoweredClient::setupRenderType);
        modEventBus.addListener(SteamPoweredClient::registerParticleFactories);
    }

    public static void clientInit(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new SPPonderPlugin());
    }
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.STEAM.get(), SteamParticle.Factory::new);
    }
    public static void setupRenderType(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DYNAMO.get(), RenderType.cutoutMipped());
        });
    }
}
