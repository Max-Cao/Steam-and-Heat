package org.nina_iseri.steam_and_heat.client.jei_plugin.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ModJEIRecipeTypes;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient.HeatUnit;
import org.nina_iseri.steam_and_heat.content.recipe.BoilerRecipe;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;

/**
 * JEI：锅炉。显示需要的 HU、水，以及产出的蒸汽。
 */
public class BoilerCategory extends CreateRecipeCategory<BoilerRecipe> {

    public BoilerCategory(Info<BoilerRecipe> info) {
        super(info);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, BoilerRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(4, 10)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ModJEIRecipeTypes.HEAT_UNIT, new HeatUnit(recipe.huPerTick()));

        builder.addSlot(RecipeIngredientRole.INPUT, 30, 10)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(NeoForgeTypes.FLUID_STACK,
                        new FluidStack(Fluids.WATER, recipe.waterPerTick()))
                .setFluidRenderer(recipe.waterPerTick(), false, 16, 16);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 10)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(NeoForgeTypes.FLUID_STACK,
                        new FluidStack(ModFluids.STEAM.get(), recipe.steamPerTick()))
                .setFluidRenderer(recipe.steamPerTick(), false, 16, 16);
    }

    @Override
    protected void draw(BoilerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics,
            double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(graphics, 52, 14);
        graphics.drawString(net.minecraft.client.Minecraft.getInstance().font,
                net.minecraft.network.chat.Component.translatable("jei.steam_and_heat.per_tick")
                        .getVisualOrderText(),
                4, 32, 0x404040, false);
    }
}
