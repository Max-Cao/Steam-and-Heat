package org.nina_iseri.steam_and_heat.registrate;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import org.nina_iseri.steam_and_heat.content.boiler.BronzeBoilerBlock;
import org.nina_iseri.steam_and_heat.content.boiler.CastIronBoilerBlock;
import org.nina_iseri.steam_and_heat.content.boiler.SteelBoilerBlock;
import org.nina_iseri.steam_and_heat.content.burner.BronzeBurnerBlock;
import org.nina_iseri.steam_and_heat.content.burner.BurnerBlockEntity;
import org.nina_iseri.steam_and_heat.content.burner.CastIronBurnerBlock;
import org.nina_iseri.steam_and_heat.content.burner.SteelBurnerBlock;
import org.nina_iseri.steam_and_heat.content.cogwheel.MetalCogwheelBlock;
import org.nina_iseri.steam_and_heat.content.engine.BronzeSteamEngineBlock;
import org.nina_iseri.steam_and_heat.content.engine.CastIronSteamEngineBlock;
import org.nina_iseri.steam_and_heat.content.engine.FurnaceEngineBlock;
import org.nina_iseri.steam_and_heat.content.engine.SteelSteamEngineBlock;
import org.nina_iseri.steam_and_heat.content.flywheel.BrassSteamFlywheelBlock;
import org.nina_iseri.steam_and_heat.content.flywheel.BronzeSteamFlywheelBlock;
import org.nina_iseri.steam_and_heat.content.flywheel.CastIronSteamFlywheelBlock;
import org.nina_iseri.steam_and_heat.content.flywheel.SteelSteamFlywheelBlock;
import org.nina_iseri.steam_and_heat.content.oldcreatestuff.OldFlywheelGenerator;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import javax.annotation.Nonnull;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static org.nina_iseri.steam_and_heat.SteamAndHeat.REGISTRATE;

public class ModBlocks {

    public static final BlockEntry<BronzeBurnerBlock> BRONZE_BURNER = REGISTRATE
            .block("bronze_burner", BronzeBurnerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CastIronBurnerBlock> CAST_IRON_BURNER = REGISTRATE
            .block("cast_iron_burner", CastIronBurnerBlock::new)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SteelBurnerBlock> STEEL_BURNER = REGISTRATE
            .block("steel_burner", SteelBurnerBlock::new)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BronzeBoilerBlock> BRONZE_BOILER = REGISTRATE
            .block("bronze_boiler", BronzeBoilerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CastIronBoilerBlock> CAST_IRON_BOILER = REGISTRATE
            .block("cast_iron_boiler", CastIronBoilerBlock::new)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SteelBoilerBlock> STEEL_BOILER = REGISTRATE
            .block("steel_boiler", SteelBoilerBlock::new)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BronzeSteamEngineBlock> BRONZE_STEAM_ENGINE = REGISTRATE
            .block("bronze_steam_engine", BronzeSteamEngineBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CastIronSteamEngineBlock> CAST_IRON_STEAM_ENGINE = REGISTRATE
            .block("cast_iron_steam_engine", CastIronSteamEngineBlock::new)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SteelSteamEngineBlock> STEEL_STEAM_ENGINE = REGISTRATE
            .block("steel_steam_engine", SteelSteamEngineBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .tag(AllTags.AllBlockTags.BRITTLE.tag)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<FurnaceEngineBlock> FURNACE_ENGINE = REGISTRATE
            .block("furnace_engine", FurnaceEngineBlock::new)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<MetalCogwheelBlock> STEEL_COGWHEEL = REGISTRATE
            .block("steel_cogwheel", MetalCogwheelBlock::small)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> STEEL_LARGE_COGWHEEL = REGISTRATE
            .block("steel_large_cogwheel", MetalCogwheelBlock::large)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> CAST_IRON_COGWHEEL = REGISTRATE
            .block("cast_iron_cogwheel", MetalCogwheelBlock::small)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> CAST_IRON_LARGE_COGWHEEL = REGISTRATE
            .block("cast_iron_large_cogwheel", MetalCogwheelBlock::large)
            .initialProperties(ModBlocks::hardMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> BRONZE_COGWHEEL = REGISTRATE
            .block("bronze_cogwheel", MetalCogwheelBlock::small)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> BRONZE_LARGE_COGWHEEL = REGISTRATE
            .block("bronze_large_cogwheel", MetalCogwheelBlock::large)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<BronzeSteamFlywheelBlock> BRONZE_FLYWHEEL = REGISTRATE
            .block("bronze_flywheel", BronzeSteamFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .blockstate(new OldFlywheelGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CastIronSteamFlywheelBlock> CAST_IRON_FLYWHEEL = REGISTRATE
            .block("cast_iron_flywheel", CastIronSteamFlywheelBlock::new)
            .initialProperties(ModBlocks::hardMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .blockstate(new OldFlywheelGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SteelSteamFlywheelBlock> STEEL_FLYWHEEL = REGISTRATE
            .block("steel_flywheel", SteelSteamFlywheelBlock::new)
            .initialProperties(ModBlocks::hardMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .blockstate(new OldFlywheelGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BrassSteamFlywheelBlock> BRASS_FLYWHEEL = REGISTRATE
            .block("brass_flywheel", BrassSteamFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .blockstate(new OldFlywheelGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {
        BoilerHeater.REGISTRY.registerProvider(ModBlocks::getHeater);
    }

    public static BoilerHeater getHeater(Block block) {
        if (BRONZE_BURNER.is(block) || CAST_IRON_BURNER.is(block) || STEEL_BURNER.is(block)) {
            return BurnerBlockEntity::getBoilerHeat;
        }
        return null;
    }

    @Nonnull
    public static Block hardMetal() {
        return Blocks.IRON_BLOCK;
    }
}
