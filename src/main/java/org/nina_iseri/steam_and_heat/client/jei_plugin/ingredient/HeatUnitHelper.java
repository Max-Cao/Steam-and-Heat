package org.nina_iseri.steam_and_heat.client.jei_plugin.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.nina_iseri.steam_and_heat.SteamAndHeat;

/**
 * 让 JEI 能搜索、复制和序列化 {@link HeatUnit}。
 */
public class HeatUnitHelper implements IIngredientHelper<HeatUnit> {

    private final IIngredientType<HeatUnit> type;

    public HeatUnitHelper(IIngredientType<HeatUnit> type) {
        this.type = type;
    }

    @Override
    public IIngredientType<HeatUnit> getIngredientType() {
        return type;
    }

    @Override
    public String getDisplayName(HeatUnit ingredient) {
        return "HU";
    }

    @Override
    @SuppressWarnings({"deprecation", "removal"})
    public String getUniqueId(HeatUnit ingredient, UidContext context) {
        // 所有 HU 数量共用同一个 UID：这样在 JEI 中查询 HU 时，
        // 120 HU、160 HU、30720 HU 等不同配方都能被一起找到。
        return "hu";
    }

    @Override
    public Object getUid(HeatUnit ingredient, UidContext context) {
        return getUniqueId(ingredient, context);
    }

    @Override
    public ResourceLocation getResourceLocation(HeatUnit ingredient) {
        return SteamAndHeat.resource("hu");
    }

    @Override
    public HeatUnit copyIngredient(HeatUnit ingredient) {
        return new HeatUnit(ingredient.amount());
    }

    @Override
    public String getErrorInfo(HeatUnit ingredient) {
        return ingredient == null ? "null HU" : ingredient.amount() + " HU";
    }

    @Override
    public long getAmount(HeatUnit ingredient) {
        return ingredient.amount();
    }

    @Override
    public HeatUnit copyWithAmount(HeatUnit ingredient, long amount) {
        return new HeatUnit((int) Math.max(1, amount));
    }

    @Override
    public ItemStack getCheatItemStack(HeatUnit ingredient) {
        // 故意不提供物品：HU 是 JEI 专用虚拟原料，无法被获取。
        return ItemStack.EMPTY;
    }
}
