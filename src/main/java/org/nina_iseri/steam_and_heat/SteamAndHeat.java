package org.nina_iseri.steam_and_heat;

import com.simibubi.create.foundation.data.CreateRegistrate;
import org.nina_iseri.steam_and_heat.client.Particles;
import org.nina_iseri.steam_and_heat.client.SteamPoweredClient;
import org.nina_iseri.steam_and_heat.content.alternator.DynamoBlockEntity;
import org.nina_iseri.steam_and_heat.content.boiler.BoilerTileEntity;
import org.nina_iseri.steam_and_heat.content.burner.BurnerBlockEntity;
import org.nina_iseri.steam_and_heat.content.engine.SteamEngineTileEntity;
import org.nina_iseri.steam_and_heat.registrate.*;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("steam_and_heat")
public class SteamAndHeat {

    public static final String MODID = "steam_and_heat";

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public SteamAndHeat(IEventBus eventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);

        if (FMLLoader.getDist() == Dist.CLIENT) {
            SteamPoweredClient.addClientListeners(NeoForge.EVENT_BUS, eventBus);
        }

        eventBus.addListener(this::registerCapabilities);
        Particles.REGISTER.register(eventBus);
        SPTabs.register(eventBus);
        SPFluids.register();
        SPBlocks.register();
        SPBlockEntities.register();
        SPItems.register();
        SPTags.init();
        new SPStress().initStress();
        modContainer.registerConfig(ModConfig.Type.COMMON, SPConfig.COMMON_CONFIG);
        modContainer.registerConfig(ModConfig.Type.SERVER, SPConfig.SERVER_CONFIG);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPBlockEntities.BRONZE_BURNER.get(), BurnerBlockEntity.ITEM_CAP);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPBlockEntities.CAST_IRON_BURNER.get(), BurnerBlockEntity.ITEM_CAP);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPBlockEntities.STEEL_BURNER.get(), BurnerBlockEntity.ITEM_CAP);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SPBlockEntities.BRONZE_BOILER.get(), BoilerTileEntity.FLUID_CAP);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SPBlockEntities.CAST_IRON_BOILER.get(), BoilerTileEntity.FLUID_CAP);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SPBlockEntities.STEEL_BOILER.get(), BoilerTileEntity.FLUID_CAP);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SPBlockEntities.BRONZE_STEAM_ENGINE.get(), SteamEngineTileEntity.FLUID_CAP);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SPBlockEntities.CAST_IRON_STEAM_ENGINE.get(), SteamEngineTileEntity.FLUID_CAP);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SPBlockEntities.STEEL_STEAM_ENGINE.get(), SteamEngineTileEntity.FLUID_CAP);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, SPBlockEntities.DYNAMO.get(), DynamoBlockEntity.ENERGY_CAP);
    }

}
