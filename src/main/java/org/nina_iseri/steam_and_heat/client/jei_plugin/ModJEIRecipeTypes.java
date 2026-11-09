package org.nina_iseri.steam_and_heat.client.jei_plugin;

import org.nina_iseri.steam_and_heat.SteamAndHeat;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;

public class ModJEIRecipeTypes {
    public interface ISolidBurnerRecipe {
        public ItemStack fuel();
    }

    public static final RecipeType<ISolidBurnerRecipe> SOLID_BURNER = 
        RecipeType.create(SteamAndHeat.MODID, "solid_burner", ISolidBurnerRecipe.class);
}