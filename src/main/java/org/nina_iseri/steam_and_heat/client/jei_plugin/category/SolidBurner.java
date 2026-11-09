package org.nina_iseri.steam_and_heat.client.jei_plugin.category;

import javax.annotation.Nullable;

import org.nina_iseri.steam_and_heat.client.jei_plugin.ModJEIRecipeTypes.ISolidBurnerRecipe;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;
import static org.nina_iseri.steam_and_heat.client.jei_plugin.ModJEIRecipeTypes.SOLID_BURNER;

import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;

import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;

import net.minecraft.network.chat.Component;

public class SolidBurner implements IRecipeCategory {
    @Override
    public Component getTitle() {
        return Component.literal("固体燃烧室燃料");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return new ItemIcon(() -> ModBlocks.BRONZE_BURNER.asStack());
    }

    @Override
    public RecipeType getRecipeType() {
        return SOLID_BURNER;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Object recipe, IFocusGroup focuses) {
        builder.addInputSlot();
    }
}
