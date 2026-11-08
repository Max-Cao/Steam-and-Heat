package org.nina_iseri.steam_and_heat;

import com.simibubi.create.foundation.data.CreateRegistrate;
import org.nina_iseri.steam_and_heat.client.ModParticles;
import org.nina_iseri.steam_and_heat.client.SteamAndHeatClient;
import org.nina_iseri.steam_and_heat.registrate.*;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("steam_and_heat")
public class SteamAndHeat {

    public static final String MODID = "steam_and_heat";

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

    public SteamAndHeat(IEventBus eventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);

        if (FMLLoader.getDist() == Dist.CLIENT) {
            SteamAndHeatClient.addClientListeners(NeoForge.EVENT_BUS, eventBus);
        }

        eventBus.addListener(this::registerCapabilities);
        ModParticles.register(eventBus);
        ModTabs.register(eventBus);
        ModFluids.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();
        ModTags.register();
        ModCommonConfig.register(modContainer);
        ModStressConfig.register();
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        ModBlockEntities.register_capabilities(event);
    }
}
