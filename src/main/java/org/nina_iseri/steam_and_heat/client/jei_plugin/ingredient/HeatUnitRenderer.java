package org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * 用类似流体罐的样式渲染 HU：黄橙色渐变、深色边框。
 */
public class HeatUnitRenderer implements IIngredientRenderer<HeatUnit> {

    private static final int BORDER = 0xFF3A2405;
    private static final int TOP = 0xFFFFD35A;
    private static final int BOTTOM = 0xFFE07A12;
    private static final int HIGHLIGHT = 0xFFFFF3B0;

    @Override
    public void render(GuiGraphics graphics, HeatUnit ingredient) {
        // 深色底
        graphics.fill(0, 0, 16, 16, BORDER);
        // 类似流体表面的黄橙色渐变
        graphics.fillGradient(1, 1, 15, 15, TOP, BOTTOM);
        // 顶部高光
        graphics.fill(2, 2, 14, 4, HIGHLIGHT);
        // 底部阴影
        graphics.fill(1, 14, 15, 15, 0xFFB85A08);
    }

    @Override
    public List<Component> getTooltip(HeatUnit ingredient, TooltipFlag tooltipFlag) {
        return List.of(
                Component.translatable("jei.steam_and_heat.hu", ingredient.amount())
                        .withStyle(ChatFormatting.GOLD),
                Component.translatable("jei.steam_and_heat.hu.description")
                        .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }
}
