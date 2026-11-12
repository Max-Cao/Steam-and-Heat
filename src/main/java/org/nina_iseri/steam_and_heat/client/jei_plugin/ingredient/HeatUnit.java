package org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient;

import com.mojang.serialization.Codec;

/**
 * JEI 中表示热量的虚拟原料。它没有对应的物品，只能作为配方查看器的输入/输出显示。
 */
public record HeatUnit(int amount) {

    public static final Codec<HeatUnit> CODEC = Codec.INT.xmap(HeatUnit::new, HeatUnit::amount);
}
