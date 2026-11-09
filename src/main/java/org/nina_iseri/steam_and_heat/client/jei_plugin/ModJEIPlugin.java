package org.nina_iseri.steam_and_heat.client.jei_plugin;

import org.jetbrains.annotations.Nullable;
import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ModJEIRecipeTypes.ISolidBurnerRecipe;
import org.nina_iseri.steam_and_heat.client.jei_plugin.category.SolidBurner;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

import static org.nina_iseri.steam_and_heat.client.jei_plugin.ModJEIRecipeTypes.SOLID_BURNER;

import java.util.List;

import com.simibubi.create.compat.jei.ItemIcon;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return SteamAndHeat.resource("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SolidBurner());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(SOLID_BURNER, List.of(
            new ISolidBurnerRecipe() {
                @Override
                public ItemStack fuel() {
                    return Items.COAL.getDefaultInstance();
                }
            }
        ));
    }
}
