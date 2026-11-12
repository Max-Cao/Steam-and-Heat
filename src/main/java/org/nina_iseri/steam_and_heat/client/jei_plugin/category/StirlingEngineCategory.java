package org.nina_iseri.steam_and_heat.client.jei_plugin.category;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ModJEIRecipeTypes;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient.HeatUnit;
import org.nina_iseri.steam_and_heat.content.recipe.EngineRecipe;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;

import java.util.List;

/**
 * JEI：斯特林引擎。单独一页列出它可以放在哪些热源方块上。
 */
public class StirlingEngineCategory extends CreateRecipeCategory<EngineRecipe> {

    private static final List<ItemStack> HEAT_SOURCES = List.of(
            ModBlocks.BRONZE_SOLID_BURNER.asStack(),
            ModBlocks.CAST_IRON_SOLID_BURNER.asStack(),
            ModBlocks.STEEL_SOLID_BURNER.asStack(),
            new ItemStack(Items.FURNACE),
            new ItemStack(Items.BLAST_FURNACE),
            new ItemStack(Items.SMOKER),
            AllBlocks.BLAZE_BURNER.asStack());

    public StirlingEngineCategory(Info<EngineRecipe> info) {
        super(info);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, EngineRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < HEAT_SOURCES.size(); i++) {
            int x = 4 + (i % 4) * 38;
            int y = 18 + (i / 4) * 22;
            builder.addSlot(mezz.jei.api.recipe.RecipeIngredientRole.INPUT, x, y)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStack(HEAT_SOURCES.get(i));
        }

        builder.addInputSlot(4, 72)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ModJEIRecipeTypes.HEAT_UNIT, new HeatUnit(recipe.inputPerTick()));
    }

    @Override
    protected void draw(EngineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics,
            double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;

        graphics.drawString(font,
                Component.translatable("jei.steam_and_heat.heat_sources").getVisualOrderText(),
                4, 3, 0x404040, false);
        graphics.drawString(font,
                Component.translatable("jei.steam_and_heat.place_on_heat_source").getVisualOrderText(),
                4, 60, 0x404040, false);

        AllGuiTextures.JEI_ARROW.render(graphics, 26, 76);

        Component rpm = Component.translatable("jei.steam_and_heat.rpm", recipe.rpm())
                .withStyle(ChatFormatting.GOLD);
        Component su = Component.translatable("jei.steam_and_heat.su", recipe.su())
                .withStyle(ChatFormatting.AQUA);
        graphics.drawString(font, rpm.getVisualOrderText(), 76, 70, 0x404040, false);
        graphics.drawString(font, su.getVisualOrderText(), 76, 82, 0x404040, false);
    }
}
