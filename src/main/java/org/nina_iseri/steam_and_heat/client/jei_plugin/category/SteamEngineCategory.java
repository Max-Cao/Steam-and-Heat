package org.nina_iseri.steam_and_heat.client.jei_plugin.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ModJEIRecipeTypes;
import org.nina_iseri.steam_and_heat.content.recipe.EngineRecipe;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;

/**
 * JEI：蒸汽引擎。单独一页说明它需要贴着锅炉使用，并列出各等级的抽汽效率。
 */
public class SteamEngineCategory extends CreateRecipeCategory<EngineRecipe> {

    public SteamEngineCategory(Info<EngineRecipe> info) {
        super(info);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, EngineRecipe recipe, IFocusGroup focuses) {
        // 三个等级放在最前面，下方显示各自效率。
        builder.addInputSlot(4, 18)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(ModBlocks.BRONZE_STEAM_ENGINE.asStack());
        builder.addInputSlot(72, 18)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(ModBlocks.CAST_IRON_STEAM_ENGINE.asStack());
        builder.addInputSlot(140, 18)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(ModBlocks.STEEL_STEAM_ENGINE.asStack());

        builder.addSlot(RecipeIngredientRole.INPUT, 4, 52)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(NeoForgeTypes.FLUID_STACK,
                        new FluidStack(ModFluids.STEAM.get(), recipe.inputPerTick()))
                .setFluidRenderer(recipe.inputPerTick(), false, 16, 16);
    }

    @Override
    protected void draw(EngineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics,
            double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;

        graphics.drawString(font,
                Component.translatable("jei.steam_and_heat.place_next_to_boiler").getVisualOrderText(),
                4, 3, 0x404040, false);

        Component efficiency = Component.literal(
                        String.format(java.util.Locale.ROOT, "%.1fx", recipe.boilerEfficiency()))
                .withStyle(ChatFormatting.GOLD);
        graphics.drawString(font, efficiency.getVisualOrderText(), 4, 38, 0x404040, false);
        graphics.drawString(font, efficiency.getVisualOrderText(), 72, 38, 0x404040, false);
        graphics.drawString(font, efficiency.getVisualOrderText(), 140, 38, 0x404040, false);

        AllGuiTextures.JEI_ARROW.render(graphics, 26, 56);

        Component rpm = Component.translatable("jei.steam_and_heat.rpm", recipe.rpm())
                .withStyle(ChatFormatting.GOLD);
        Component su = Component.translatable("jei.steam_and_heat.su", recipe.su())
                .withStyle(ChatFormatting.AQUA);
        graphics.drawString(font, rpm.getVisualOrderText(), 76, 50, 0x404040, false);
        graphics.drawString(font, su.getVisualOrderText(), 76, 62, 0x404040, false);
    }
}
