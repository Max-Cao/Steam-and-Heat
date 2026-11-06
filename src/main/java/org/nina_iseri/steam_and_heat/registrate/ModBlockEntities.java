package org.nina_iseri.steam_and_heat.registrate;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import org.nina_iseri.steam_and_heat.client.instance.BrassFlywheelInstance;
import org.nina_iseri.steam_and_heat.client.instance.BronzeFlywheelInstance;
import org.nina_iseri.steam_and_heat.client.instance.CastIronFlywheelInstance;
import org.nina_iseri.steam_and_heat.client.instance.SteelFlywheelInstance;
import org.nina_iseri.steam_and_heat.client.render.BrassFlywheelRenderer;
import org.nina_iseri.steam_and_heat.client.render.BronzeFlywheelRenderer;
import org.nina_iseri.steam_and_heat.client.render.CastIronFlywheelRenderer;
import org.nina_iseri.steam_and_heat.client.render.SteelFlywheelRenderer;
import org.nina_iseri.steam_and_heat.content.alternator.DynamoBlockEntity;
import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlockEntity;
import org.nina_iseri.steam_and_heat.content.boiler.BronzeBoilerBlockEntity;
import org.nina_iseri.steam_and_heat.content.boiler.CastIronBoilerBlockEntity;
import org.nina_iseri.steam_and_heat.content.boiler.SteelBoilerBlockEntity;
import org.nina_iseri.steam_and_heat.content.burner.BronzeBurnerBlockEntity;
import org.nina_iseri.steam_and_heat.content.burner.BurnerBlockEntity;
import org.nina_iseri.steam_and_heat.content.burner.CastIronBurnerBlockEntity;
import org.nina_iseri.steam_and_heat.content.burner.SteelBurnerBlockEntity;
import org.nina_iseri.steam_and_heat.content.cogwheel.MetalCogwheelBlockEntity;
import org.nina_iseri.steam_and_heat.content.engine.BronzeSteamEngineTileEntity;
import org.nina_iseri.steam_and_heat.content.engine.CastIronSteamEngineTileEntity;
import org.nina_iseri.steam_and_heat.content.engine.FurnaceEngineTileEntity;
import org.nina_iseri.steam_and_heat.content.engine.SteamEngineTileEntity;
import org.nina_iseri.steam_and_heat.content.engine.SteelSteamEngineTileEntity;
import org.nina_iseri.steam_and_heat.content.flywheel.SteamFlywheelTileEntity;
import org.nina_iseri.steam_and_heat.oldcreatestuff.OldEngineInstance;
import org.nina_iseri.steam_and_heat.oldcreatestuff.OldEngineRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static org.nina_iseri.steam_and_heat.SteamAndHeat.REGISTRATE;

public class ModBlockEntities {

        public static final BlockEntityEntry<BronzeBurnerBlockEntity> BRONZE_BURNER = REGISTRATE
                        .blockEntity("bronze_burner", BronzeBurnerBlockEntity::new)
                        .validBlocks(ModBlocks.BRONZE_BURNER)
                        .register();

        public static final BlockEntityEntry<CastIronBurnerBlockEntity> CAST_IRON_BURNER = REGISTRATE
                        .blockEntity("cast_iron_burner", CastIronBurnerBlockEntity::new)
                        .validBlocks(ModBlocks.CAST_IRON_BURNER)
                        .register();

        public static final BlockEntityEntry<SteelBurnerBlockEntity> STEEL_BURNER = REGISTRATE
                        .blockEntity("steel_burner", SteelBurnerBlockEntity::new)
                        .validBlocks(ModBlocks.STEEL_BURNER)
                        .register();

        public static final BlockEntityEntry<BronzeBoilerBlockEntity> BRONZE_BOILER = REGISTRATE
                        .blockEntity("bronze_boiler", BronzeBoilerBlockEntity::new)
                        .validBlocks(ModBlocks.BRONZE_BOILER)
                        .register();

        public static final BlockEntityEntry<CastIronBoilerBlockEntity> CAST_IRON_BOILER = REGISTRATE
                        .blockEntity("cast_iron_boiler", CastIronBoilerBlockEntity::new)
                        .validBlocks(ModBlocks.CAST_IRON_BOILER)
                        .register();

        public static final BlockEntityEntry<SteelBoilerBlockEntity> STEEL_BOILER = REGISTRATE
                        .blockEntity("steel_boiler", SteelBoilerBlockEntity::new)
                        .validBlocks(ModBlocks.STEEL_BOILER)
                        .register();

        public static final BlockEntityEntry<BronzeSteamEngineTileEntity> BRONZE_STEAM_ENGINE = REGISTRATE
                        .blockEntity("bronze_steam_engine", BronzeSteamEngineTileEntity::new)
                        .validBlocks(ModBlocks.BRONZE_STEAM_ENGINE)
                        .register();

        public static final BlockEntityEntry<CastIronSteamEngineTileEntity> CAST_IRON_STEAM_ENGINE = REGISTRATE
                        .blockEntity("cast_iron_steam_engine", CastIronSteamEngineTileEntity::new)
                        .validBlocks(ModBlocks.CAST_IRON_STEAM_ENGINE)
                        .register();

        public static final BlockEntityEntry<SteelSteamEngineTileEntity> STEEL_STEAM_ENGINE = REGISTRATE
                        .blockEntity("steel_steam_engine", SteelSteamEngineTileEntity::new)
                        .validBlocks(ModBlocks.STEEL_STEAM_ENGINE)
                        .register();

        public static final BlockEntityEntry<FurnaceEngineTileEntity> FURNACE_ENGINE = REGISTRATE
                        .blockEntity("furnace_engine", FurnaceEngineTileEntity::new)
                        .visual(() -> OldEngineInstance::new, false)
                        .validBlocks(ModBlocks.FURNACE_ENGINE)
                        .renderer(() -> OldEngineRenderer::new)
                        .register();

        public static final BlockEntityEntry<MetalCogwheelBlockEntity> METAL_COGWHEEL = REGISTRATE
                        .blockEntity("metal_cogwheel", MetalCogwheelBlockEntity::new)
                        .visual(() -> SingleAxisRotatingVisual::shaft)
                        .validBlocks(ModBlocks.STEEL_COGWHEEL, ModBlocks.STEEL_LARGE_COGWHEEL,
                                        ModBlocks.CAST_IRON_COGWHEEL, ModBlocks.CAST_IRON_LARGE_COGWHEEL,
                                        ModBlocks.BRONZE_COGWHEEL, ModBlocks.BRONZE_LARGE_COGWHEEL)
                        .renderer(() -> KineticBlockEntityRenderer::new)
                        .register();

        public static final BlockEntityEntry<DynamoBlockEntity> DYNAMO = REGISTRATE
                        .blockEntity("alternator", DynamoBlockEntity::new)
                        .visual(() -> org.nina_iseri.steam_and_heat.content.alternator.DynamoShaftInstance::new)
                        .validBlocks(ModBlocks.DYNAMO)
                        .register();

        public static final BlockEntityEntry<SteamFlywheelTileEntity> BRONZE_STEAM_FLYWHEEL = REGISTRATE
                        .blockEntity("bronze_steam_flywheel", SteamFlywheelTileEntity::new)
                        .visual(() -> BronzeFlywheelInstance::new)
                        .validBlocks(ModBlocks.BRONZE_FLYWHEEL)
                        .renderer(() -> BronzeFlywheelRenderer::new)
                        .register();

        public static final BlockEntityEntry<SteamFlywheelTileEntity> CAST_IRON_STEAM_FLYWHEEL = REGISTRATE
                        .blockEntity("cast_iron_steam_flywheel", SteamFlywheelTileEntity::new)
                        .visual(() -> CastIronFlywheelInstance::new)
                        .validBlocks(ModBlocks.CAST_IRON_FLYWHEEL)
                        .renderer(() -> CastIronFlywheelRenderer::new)
                        .register();

        public static final BlockEntityEntry<SteamFlywheelTileEntity> STEEL_STEAM_FLYWHEEL = REGISTRATE
                        .blockEntity("steel_steam_flywheel", SteamFlywheelTileEntity::new)
                        .visual(() -> SteelFlywheelInstance::new)
                        .validBlocks(ModBlocks.STEEL_FLYWHEEL)
                        .renderer(() -> SteelFlywheelRenderer::new)
                        .register();

        public static final BlockEntityEntry<SteamFlywheelTileEntity> BRASS_STEAM_FLYWHEEL = REGISTRATE
                        .blockEntity("brass_steam_flywheel", SteamFlywheelTileEntity::new)
                        .visual(() -> BrassFlywheelInstance::new)
                        .validBlocks(ModBlocks.BRASS_FLYWHEEL)
                        .renderer(() -> BrassFlywheelRenderer::new)
                        .register();

        public static void register() {
        }

        public static void register_capabilities(RegisterCapabilitiesEvent event) {
                event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.BRONZE_BURNER.get(),
                                BurnerBlockEntity.ITEM_CAP);
                event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.CAST_IRON_BURNER.get(),
                                BurnerBlockEntity.ITEM_CAP);
                event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.STEEL_BURNER.get(),
                                BurnerBlockEntity.ITEM_CAP);
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.BRONZE_BOILER.get(),
                                BoilerBlockEntity.FLUID_CAP);
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.CAST_IRON_BOILER.get(),
                                BoilerBlockEntity.FLUID_CAP);
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.STEEL_BOILER.get(),
                                BoilerBlockEntity.FLUID_CAP);
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.BRONZE_STEAM_ENGINE.get(),
                                SteamEngineTileEntity.FLUID_CAP);
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                                ModBlockEntities.CAST_IRON_STEAM_ENGINE.get(), SteamEngineTileEntity.FLUID_CAP);
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.STEEL_STEAM_ENGINE.get(),
                                SteamEngineTileEntity.FLUID_CAP);
                event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.DYNAMO.get(),
                                DynamoBlockEntity.ENERGY_CAP);
        }
}
