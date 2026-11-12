package org.nina_iseri.steam_and_heat.client.jei_plugin;

import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient.HeatUnit;
import org.nina_iseri.steam_and_heat.content.recipe.BoilerRecipe;
import org.nina_iseri.steam_and_heat.content.recipe.EngineRecipe;
import org.nina_iseri.steam_and_heat.content.recipe.ModRecipes;
import org.nina_iseri.steam_and_heat.content.recipe.SolidBurnerRecipe;

public final class ModJEIRecipeTypes {

    /** JEI 中的虚拟热量原料。 */
    public static final IIngredientType<HeatUnit> HEAT_UNIT = () -> HeatUnit.class;

    public static final RecipeType<RecipeHolder<SolidBurnerRecipe>> SOLID_BURNER =
            RecipeType.createRecipeHolderType(ModRecipes.SOLID_BURNER_ID);
    public static final RecipeType<RecipeHolder<BoilerRecipe>> BOILER =
            RecipeType.createRecipeHolderType(ModRecipes.BOILER_ID);
    public static final RecipeType<RecipeHolder<EngineRecipe>> STEAM_ENGINE =
            RecipeType.createRecipeHolderType(SteamAndHeat.resource("steam_engine"));
    public static final RecipeType<RecipeHolder<EngineRecipe>> STIRLING_ENGINE =
            RecipeType.createRecipeHolderType(SteamAndHeat.resource("stirling_engine"));

    private ModJEIRecipeTypes() {
    }
}
