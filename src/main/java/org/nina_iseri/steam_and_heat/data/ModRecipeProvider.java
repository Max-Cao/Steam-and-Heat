package org.nina_iseri.steam_and_heat.data;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.content.recipe.BoilerRecipe;
import org.nina_iseri.steam_and_heat.content.recipe.EngineRecipe;
import org.nina_iseri.steam_and_heat.content.recipe.SolidBurnerRecipe;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;
import org.nina_iseri.steam_and_heat.registrate.ModItems;
import org.nina_iseri.steam_and_heat.registrate.ModTags;

import java.util.List;

import static org.nina_iseri.steam_and_heat.SteamAndHeat.REGISTRATE;

/**
 * 模组全部配方的数据生成入口。
 *
 * <p>这里同时生成原版工作台配方、Create 的机械动力合成配方，以及本模组自己的
 * 燃烧室 / 锅炉 / 引擎机器配方。</p>
 */
public final class ModRecipeProvider {

    /** 燃料燃烧时间（tick）。 */
    private static final int COAL_BURN_TICKS = 1600;
    private static final int COAL_BLOCK_BURN_TICKS = 16000;
    /** 每个燃料 tick 对应的基础 HU。 */
    private static final int HU_PER_FUEL_TICK = 24;

    private ModRecipeProvider() {
    }

    public static void register() {
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, ModRecipeProvider::generate);
    }

    private static void generate(RegistrateRecipeProvider output) {
        // 注：旧的 alternator 配方对应的方块已不再注册，因此这里不再生成该配方，
        // 其遗留的资源也一并清理，避免服务器加载时出现未知方块引用。
        // 锅炉
        shapedBoiler(output, ModBlocks.BRONZE_BOILER.get(), modTag("bronzeplate"));
        shapedBoiler(output, ModBlocks.CAST_IRON_BOILER.get(), commonTag("plates/iron"));
        shapedBoiler(output, ModBlocks.STEEL_BOILER.get(), commonTag("plates/steel"));

        // 固体燃烧室
        shapedBurner(output, ModBlocks.BRONZE_SOLID_BURNER.get(), modTag("bronzeplate"));
        shapedBurner(output, ModBlocks.CAST_IRON_SOLID_BURNER.get(), commonTag("plates/iron"));
        shapedBurner(output, ModBlocks.STEEL_SOLID_BURNER.get(), commonTag("plates/steel"));

        // 齿轮
        shapedCogwheel(output, ModBlocks.BRONZE_COGWHEEL.get(), modTag("bronze"));
        shapedCogwheel(output, ModBlocks.CAST_IRON_COGWHEEL.get(), commonTag("ingots/iron"));
        shapedCogwheel(output, ModBlocks.STEEL_COGWHEEL.get(), commonTag("ingots/steel"));

        shapedLargeCogwheel(output, ModBlocks.BRONZE_LARGE_COGWHEEL.get(), modTag("bronze"),
                modTag("bronzeplate"));
        shapedLargeCogwheel(output, ModBlocks.CAST_IRON_LARGE_COGWHEEL.get(), commonTag("ingots/iron"),
                commonTag("plates/iron"));
        shapedLargeCogwheel(output, ModBlocks.STEEL_LARGE_COGWHEEL.get(), commonTag("ingots/steel"),
                commonTag("plates/steel"));

        // 飞轮
        mechanicalFlywheel(output, ModBlocks.BRONZE_FLYWHEEL.get(), modTag("bronze"),
                ModBlocks.BRONZE_COGWHEEL.get());
        mechanicalFlywheel(output, ModBlocks.CAST_IRON_FLYWHEEL.get(), commonTag("ingots/iron"),
                ModBlocks.CAST_IRON_COGWHEEL.get());
        mechanicalFlywheel(output, ModBlocks.STEEL_FLYWHEEL.get(), commonTag("ingots/steel"),
                ModBlocks.STEEL_COGWHEEL.get());

        MechanicalCraftingRecipeBuilder.shapedRecipe(ModBlocks.BRASS_FLYWHEEL.get())
                .patternLine(" BBB")
                .patternLine("CB B")
                .patternLine(" BBB")
                .key('B', commonTag("ingots/brass"))
                .key('C', AllBlocks.BRASS_CASING.get())
                .build(output);

        // 蒸汽引擎
        mechanicalSteamEngine(output, ModBlocks.BRONZE_STEAM_ENGINE.get(), modTag("bronzeplate"),
                modTag("bronze"));
        mechanicalSteamEngine(output, ModBlocks.CAST_IRON_STEAM_ENGINE.get(), commonTag("plates/iron"),
                commonTag("ingots/iron"));
        mechanicalSteamEngine(output, ModBlocks.STEEL_STEAM_ENGINE.get(), commonTag("plates/steel"),
                commonTag("ingots/steel"));

        // 斯特林引擎
        MechanicalCraftingRecipeBuilder.shapedRecipe(ModBlocks.STIRLING_ENGINE.get())
                .patternLine("PPB")
                .patternLine("PCI")
                .patternLine("PPB")
                .key('P', commonTag("plates/brass"))
                .key('B', commonTag("ingots/brass"))
                .key('I', Ingredient.of(Items.PISTON, Items.STICKY_PISTON))
                .key('C', AllBlocks.BRASS_CASING.get())
                .build(output);

        // 容器
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PRESSURIZED_GAS_CONTAINER.get(), 2)
                .pattern("SCS")
                .pattern("SCS")
                .pattern("SCS")
                .define('S', commonTag("plates/copper"))
                .define('C', commonTag("glass_blocks/colorless"))
                .unlockedBy("has_copper_plate", has(commonTag("plates/copper")))
                .save(output, SteamAndHeat.resource("pressurized_gas_container"));

        // 蒸汽容器装填/排空
        new StandardProcessingRecipe.Builder<>(FillingRecipe::new,
                SteamAndHeat.resource("pressurized_steam_container"))
                .require(ModItems.PRESSURIZED_GAS_CONTAINER.get())
                .require(ModTags.STEAM, 1000)
                .output(ModItems.PRESSURIZED_STEAM_CONTAINER.get())
                .build(output);

        new StandardProcessingRecipe.Builder<>(EmptyingRecipe::new,
                SteamAndHeat.resource("pressurized_steam_container"))
                .require(ModItems.PRESSURIZED_STEAM_CONTAINER.get())
                .output(ModItems.PRESSURIZED_GAS_CONTAINER.get())
                .output(ModFluids.STEAM.get(), 1000)
                .build(output);

        // 机器配方
        burnerRecipes(output);
        boilerRecipes(output);
        engineRecipes(output);
    }

    private static void shapedBoiler(RecipeOutput output, ItemLike result, TagKey<Item> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 1)
                .pattern("SSS")
                .pattern("SCS")
                .pattern("SCS")
                .define('S', material)
                .define('C', AllBlocks.FLUID_PIPE.get())
                .unlockedBy("has_material", has(material))
                .save(output, SteamAndHeat.resource(name(result)));
    }

    private static void shapedBurner(RecipeOutput output, ItemLike result, TagKey<Item> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 1)
                .pattern("SSS")
                .pattern("S S")
                .pattern("CCC")
                .define('S', material)
                .define('C', Items.BRICKS)
                .unlockedBy("has_material", has(material))
                .save(output, SteamAndHeat.resource(name(result)));
    }

    private static void shapedCogwheel(RecipeOutput output, ItemLike result, TagKey<Item> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 8)
                .pattern("SSS")
                .pattern("SCS")
                .pattern("SSS")
                .define('S', material)
                .define('C', AllItems.ANDESITE_ALLOY.get())
                .unlockedBy("has_material", has(material))
                .save(output, SteamAndHeat.resource(name(result)));
    }

    private static void shapedLargeCogwheel(RecipeOutput output, ItemLike result, TagKey<Item> material,
            TagKey<Item> plate) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 8)
                .pattern("SDS")
                .pattern("DCD")
                .pattern("SDS")
                .define('S', material)
                .define('D', plate)
                .define('C', AllItems.ANDESITE_ALLOY.get())
                .unlockedBy("has_material", has(material))
                .save(output, SteamAndHeat.resource(name(result)));
    }

    private static void mechanicalFlywheel(RecipeOutput output, ItemLike result, TagKey<Item> material,
            ItemLike cogwheel) {
        MechanicalCraftingRecipeBuilder.shapedRecipe(result)
                .patternLine(" AAA ")
                .patternLine("ACPCA")
                .patternLine("APSPA")
                .patternLine("ACPCA")
                .patternLine(" AAA ")
                .key('A', material)
                .key('C', cogwheel)
                .key('P', ItemTags.PLANKS)
                .key('S', AllBlocks.SHAFT.get())
                .disallowMirrored()
                .build(output);
    }

    private static void mechanicalSteamEngine(RecipeOutput output, ItemLike result, TagKey<Item> plate,
            TagKey<Item> material) {
        MechanicalCraftingRecipeBuilder.shapedRecipe(result)
                .patternLine("PCB")
                .patternLine("PCI")
                .patternLine("PCB")
                .key('P', plate)
                .key('B', material)
                .key('I', Ingredient.of(Items.PISTON, Items.STICKY_PISTON))
                .key('C', AllBlocks.FLUID_PIPE.get())
                .build(output);
    }

    private static void burnerRecipes(RecipeOutput output) {
        solidBurner(output, "bronze", ModBlocks.BRONZE_SOLID_BURNER.get(), 120, 0.8D);
        solidBurner(output, "cast_iron", ModBlocks.CAST_IRON_SOLID_BURNER.get(), 240, 0.9D);
        solidBurner(output, "steel", ModBlocks.STEEL_SOLID_BURNER.get(), 480, 1.0D);
    }

    private static void solidBurner(RecipeOutput output, String tier, ItemLike machine, int huPerTick,
            double efficiency) {
        List<SolidBurnerRecipe.Fuel> fuels = List.of(
                fuel(Ingredient.of(Items.COAL), COAL_BURN_TICKS, efficiency),
                fuel(Ingredient.of(Items.CHARCOAL), COAL_BURN_TICKS, efficiency),
                fuel(Ingredient.of(commonTag("storage_blocks/coal")), COAL_BLOCK_BURN_TICKS, efficiency),
                fuel(Ingredient.of(commonTag("storage_blocks/charcoal")), COAL_BLOCK_BURN_TICKS, efficiency));

        output.accept(SteamAndHeat.resource("solid_burner/" + tier),
                new SolidBurnerRecipe(Ingredient.of(machine), huPerTick, fuels), null);
    }

    private static SolidBurnerRecipe.Fuel fuel(Ingredient ingredient, int burnTicks, double efficiency) {
        return new SolidBurnerRecipe.Fuel(ingredient,
                (int) Math.round(burnTicks * HU_PER_FUEL_TICK * efficiency));
    }

    private static void boilerRecipes(RecipeOutput output) {
        boiler(output, "bronze", ModBlocks.BRONZE_BOILER.get(), 120, 1, 12);
        boiler(output, "cast_iron", ModBlocks.CAST_IRON_BOILER.get(), 240, 2, 24);
        boiler(output, "steel", ModBlocks.STEEL_BOILER.get(), 480, 4, 48);
    }

    private static void boiler(RecipeOutput output, String tier, ItemLike machine, int huPerTick, int waterPerTick,
            int steamPerTick) {
        output.accept(SteamAndHeat.resource("boiler/" + tier),
                new BoilerRecipe(Ingredient.of(machine), huPerTick, waterPerTick, steamPerTick), null);
    }

    private static void engineRecipes(RecipeOutput output) {
        engine(output, "stirling", ModBlocks.STIRLING_ENGINE.get(), EngineRecipe.InputType.HU, 160, 16, 1024, 1.0D);
        engine(output, "bronze", ModBlocks.BRONZE_STEAM_ENGINE.get(), EngineRecipe.InputType.STEAM, 12, 32, 512,
                0.7D);
        engine(output, "cast_iron", ModBlocks.CAST_IRON_STEAM_ENGINE.get(), EngineRecipe.InputType.STEAM, 24, 32,
                1024, 0.7D);
        engine(output, "steel", ModBlocks.STEEL_STEAM_ENGINE.get(), EngineRecipe.InputType.STEAM, 48, 32, 2048,
                0.7D);
    }

    private static void engine(RecipeOutput output, String tier, ItemLike machine, EngineRecipe.InputType input,
            int inputPerTick, float rpm, float su, double boilerEfficiency) {
        output.accept(SteamAndHeat.resource("engine/" + tier),
                new EngineRecipe(Ingredient.of(machine), input, inputPerTick, rpm, su, boilerEfficiency), null);
    }

    private static TagKey<Item> commonTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static TagKey<Item> modTag(String path) {
        return ItemTags.create(SteamAndHeat.resource(path));
    }

    private static Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    private static String name(ItemLike item) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }
}
