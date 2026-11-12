package org.nina_iseri.steam_and_heat.client.jei_plugin;

import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.client.jei_plugin.category.BoilerCategory;
import org.nina_iseri.steam_and_heat.client.jei_plugin.category.SolidBurnerCategory;
import org.nina_iseri.steam_and_heat.client.jei_plugin.category.SteamEngineCategory;
import org.nina_iseri.steam_and_heat.client.jei_plugin.category.StirlingEngineCategory;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient.HeatUnit;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient.HeatUnitHelper;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient.HeatUnitRenderer;
import org.nina_iseri.steam_and_heat.content.recipe.ModRecipes;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {

    private List<CreateRecipeCategory<?>> categories = List.of();

    @Override
    public ResourceLocation getPluginUid() {
        return SteamAndHeat.resource("jei_plugin");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(ModJEIRecipeTypes.HEAT_UNIT, List.of(new HeatUnit(1)),
                new HeatUnitHelper(ModJEIRecipeTypes.HEAT_UNIT), new HeatUnitRenderer(), HeatUnit.CODEC);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        categories = List.of(
                new SolidBurnerCategory(new CreateRecipeCategory.Info<>(
                        ModJEIRecipeTypes.SOLID_BURNER,
                        Component.translatable("jei.steam_and_heat.solid_burner"),
                        new EmptyBackground(180, 140),
                        new ItemIcon(() -> ModBlocks.BRONZE_SOLID_BURNER.asStack()),
                        // 三个等级的燃料列表相同，只在页面顶部显示倍率；这里取钢等级作为 ×1.0 基准。
                        () -> singleRecipe(ModRecipes.SOLID_BURNER_TYPE.get(), ModBlocks.STEEL_SOLID_BURNER.get()),
                        List.of(
                                () -> ModBlocks.BRONZE_SOLID_BURNER.asStack(),
                                () -> ModBlocks.CAST_IRON_SOLID_BURNER.asStack(),
                                () -> ModBlocks.STEEL_SOLID_BURNER.asStack()))),
                new BoilerCategory(new CreateRecipeCategory.Info<>(
                        ModJEIRecipeTypes.BOILER,
                        Component.translatable("jei.steam_and_heat.boiler"),
                        new EmptyBackground(150, 44),
                        new ItemIcon(() -> ModBlocks.BRONZE_BOILER.asStack()),
                        // 三个等级的数值只是等比例放大，只展示一份即可。
                        () -> singleRecipe(ModRecipes.BOILER_TYPE.get(), ModBlocks.BRONZE_BOILER.get()),
                        List.of(
                                () -> ModBlocks.BRONZE_BOILER.asStack(),
                                () -> ModBlocks.CAST_IRON_BOILER.asStack(),
                                () -> ModBlocks.STEEL_BOILER.asStack()))),
                new SteamEngineCategory(new CreateRecipeCategory.Info<>(
                        ModJEIRecipeTypes.STEAM_ENGINE,
                        Component.translatable("jei.steam_and_heat.steam_engine"),
                        new EmptyBackground(180, 80),
                        new ItemIcon(() -> ModBlocks.BRONZE_STEAM_ENGINE.asStack()),
                        () -> singleRecipe(ModRecipes.ENGINE_TYPE.get(), ModBlocks.BRONZE_STEAM_ENGINE.get()),
                        List.of(
                                () -> ModBlocks.BRONZE_STEAM_ENGINE.asStack(),
                                () -> ModBlocks.CAST_IRON_STEAM_ENGINE.asStack(),
                                () -> ModBlocks.STEEL_STEAM_ENGINE.asStack()))),
                new StirlingEngineCategory(new CreateRecipeCategory.Info<>(
                        ModJEIRecipeTypes.STIRLING_ENGINE,
                        Component.translatable("jei.steam_and_heat.stirling_engine"),
                        new EmptyBackground(190, 100),
                        new ItemIcon(() -> ModBlocks.STIRLING_ENGINE.asStack()),
                        () -> singleRecipe(ModRecipes.ENGINE_TYPE.get(), ModBlocks.STIRLING_ENGINE.get()),
                        List.of(() -> ModBlocks.STIRLING_ENGINE.asStack()))));
        categories.forEach(registration::addRecipeCategories);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        categories.forEach(category -> category.registerRecipes(registration));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        categories.forEach(category -> category.registerCatalysts(registration));
    }

    private static <T extends ModRecipes.MachineRecipe> List<RecipeHolder<T>> singleRecipe(
            net.minecraft.world.item.crafting.RecipeType<T> type, ItemLike machine) {
        List<RecipeHolder<T>> all = recipes(type);
        ItemStack machineStack = new ItemStack(machine);
        for (RecipeHolder<T> holder : all) {
            if (holder.value().machine().test(machineStack)) {
                return List.of(holder);
            }
        }
        return all.isEmpty() ? List.of() : List.of(all.get(0));
    }

    private static <T extends ModRecipes.MachineRecipe> List<RecipeHolder<T>> recipes(
            net.minecraft.world.item.crafting.RecipeType<T> type) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }
        return level.getRecipeManager().getAllRecipesFor(type);
    }
}
