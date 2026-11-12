package org.nina_iseri.steam_and_heat.content.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.nina_iseri.steam_and_heat.SteamAndHeat;

import java.util.Optional;

/**
 * 蒸汽与热能的机器配方类型。
 *
 * <p>这些配方不参与原版工作台合成，而是给燃烧室、锅炉和引擎在运行时查询数值使用。
 * 每个配方都带有一个 {@code machine} 字段（机器方块物品），因此同一个配方类型可以
 * 同时描述青铜、铸铁、钢三种等级，并且可以通过数据包覆盖。</p>
 */
public final class ModRecipes {

    public static final ResourceLocation SOLID_BURNER_ID = SteamAndHeat.resource("solid_burner");
    public static final ResourceLocation BOILER_ID = SteamAndHeat.resource("boiler");
    public static final ResourceLocation ENGINE_ID = SteamAndHeat.resource("engine");

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, SteamAndHeat.MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, SteamAndHeat.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SolidBurnerRecipe>> SOLID_BURNER_TYPE =
            RECIPE_TYPES.register("solid_burner", () -> RecipeType.simple(SOLID_BURNER_ID));
    public static final DeferredHolder<RecipeType<?>, RecipeType<BoilerRecipe>> BOILER_TYPE =
            RECIPE_TYPES.register("boiler", () -> RecipeType.simple(BOILER_ID));
    public static final DeferredHolder<RecipeType<?>, RecipeType<EngineRecipe>> ENGINE_TYPE =
            RECIPE_TYPES.register("engine", () -> RecipeType.simple(ENGINE_ID));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SolidBurnerRecipe>> SOLID_BURNER_SERIALIZER =
            RECIPE_SERIALIZERS.register("solid_burner", SolidBurnerRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BoilerRecipe>> BOILER_SERIALIZER =
            RECIPE_SERIALIZERS.register("boiler", BoilerRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EngineRecipe>> ENGINE_SERIALIZER =
            RECIPE_SERIALIZERS.register("engine", EngineRecipe.Serializer::new);

    private ModRecipes() {
    }

    public static void register(IEventBus modBus) {
        RECIPE_TYPES.register(modBus);
        RECIPE_SERIALIZERS.register(modBus);
    }

    public static Optional<SolidBurnerRecipe> findBurner(Level level, ItemStack machine) {
        return find(level, SOLID_BURNER_TYPE.get(), machine);
    }

    public static Optional<BoilerRecipe> findBoiler(Level level, ItemStack machine) {
        return find(level, BOILER_TYPE.get(), machine);
    }

    public static Optional<EngineRecipe> findEngine(Level level, ItemStack machine) {
        return find(level, ENGINE_TYPE.get(), machine);
    }

    private static <T extends MachineRecipe> Optional<T> find(Level level, RecipeType<T> type, ItemStack machine) {
        if (level == null || machine.isEmpty()) {
            return Optional.empty();
        }
        return level.getRecipeManager()
                .getAllRecipesFor(type)
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.machine().test(machine))
                .findFirst();
    }

    /**
     * 所有机器配方都通过 {@code machine} 字段匹配对应的机器方块。
     */
    public interface MachineRecipe extends Recipe<SingleRecipeInput> {
        Ingredient machine();

        /**
         * 机器配方不是工作台配方，空的可选燃料标签（例如未安装的 charcoal block 标签）
         * 不应该让整个配方被判定为“不完整”。
         */
        @Override
        default boolean isIncomplete() {
            return false;
        }
    }
}
