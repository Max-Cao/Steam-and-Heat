package org.nina_iseri.steam_and_heat.content.heat;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.nina_iseri.steam_and_heat.SteamAndHeat;

import java.util.List;
import java.util.Locale;

/**
 * 统一的热源/热量护目镜提示，表现方式与 Create 的流体容器提示类似。
 *
 * <p>所有提供 {@link IHeatHandler} 能力的本模组方块都应调用这里，避免每个方块
 * 自己写一套格式。</p>
 */
public final class HeatGoggles {

    private HeatGoggles() {
    }

    public static boolean addToTooltip(List<Component> tooltip, @Nullable IHeatHandler heat) {
        if (heat == null) {
            return false;
        }

        new LangBuilder(SteamAndHeat.MODID)
                .translate("gui.goggles.heat_container")
                .forGoggles(tooltip);

        double capacity = heat.getHeatCapacity();
        String capacityText = capacity >= Double.MAX_VALUE / 2 ? "∞" : format(capacity);
        new LangBuilder(SteamAndHeat.MODID)
                .translate("gui.goggles.heat_amount", format(heat.getHeatAmount()), capacityText)
                .style(ChatFormatting.GOLD)
                .forGoggles(tooltip, 1);
        return true;
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, "%,.0f", value);
    }
}
