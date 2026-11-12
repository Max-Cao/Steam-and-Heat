package org.nina_iseri.steam_and_heat.client.jei_plugin.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ModJEIRecipeTypes;
import org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient.HeatUnit;
import org.nina_iseri.steam_and_heat.content.recipe.SolidBurnerRecipe;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JEI：固体燃烧室。
 *
 * <p>燃料只显示一次；最前面集中列出三个等级的燃烧倍率，下面的 HU 以钢等级（×1.0）为基准。</p>
 */
public class SolidBurnerCategory extends CreateRecipeCategory<SolidBurnerRecipe> {

    public SolidBurnerCategory(Info<SolidBurnerRecipe> info) {
        super(info);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, SolidBurnerRecipe recipe, IFocusGroup focuses) {
        int y = 54;
        for (UniqueFuel fuel : uniqueFuels(recipe)) {
            builder.addInputSlot(20, y)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStack(fuel.stack());
            builder.addOutputSlot(120, y)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredient(ModJEIRecipeTypes.HEAT_UNIT, new HeatUnit(fuel.hu()));
            y += 20;
        }
    }

    @Override
    protected void draw(SolidBurnerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics,
            double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;

        graphics.drawString(font,
                Component.translatable("jei.steam_and_heat.burn_multiplier").getVisualOrderText(),
                4, 3, 0x404040, false);
        graphics.drawString(font, tierLine("bronze", 120, 0.8F).getVisualOrderText(), 4, 14, 0x404040, false);
        graphics.drawString(font, tierLine("cast_iron", 240, 0.9F).getVisualOrderText(), 4, 25, 0x404040, false);
        graphics.drawString(font, tierLine("steel", 480, 1.0F).getVisualOrderText(), 4, 36, 0x404040, false);

        graphics.drawString(font,
                Component.translatable("jei.steam_and_heat.hu_at_1x")
                        .withStyle(ChatFormatting.GOLD).getVisualOrderText(),
                100, 40, 0x404040, false);

        int y = 54;
        for (int i = 0; i < uniqueFuels(recipe).size(); i++) {
            AllGuiTextures.JEI_ARROW.render(graphics, 44, y + 4);
            y += 20;
        }
    }

    private static Component tierLine(String tier, int huPerTick, float multiplier) {
        return Component.translatable("jei.steam_and_heat.burner_tier",
                Component.translatable("jei.steam_and_heat.tier." + tier),
                huPerTick, multiplier);
    }

    private static List<UniqueFuel> uniqueFuels(SolidBurnerRecipe recipe) {
        Map<Item, UniqueFuel> unique = new LinkedHashMap<>();
        for (SolidBurnerRecipe.Fuel fuel : recipe.fuels()) {
            for (ItemStack stack : fuel.ingredient().getItems()) {
                unique.putIfAbsent(stack.getItem(), new UniqueFuel(stack.copyWithCount(1), fuel.hu()));
            }
        }
        return List.copyOf(unique.values());
    }

    private record UniqueFuel(ItemStack stack, int hu) {
    }
}
