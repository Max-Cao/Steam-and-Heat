package org.nina_iseri.steam_and_heat.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * 固体燃烧室配方：某种燃料可以释放多少 HU，以及燃烧室每 tick 最多输出多少 HU。
 */
public class SolidBurnerRecipe implements ModRecipes.MachineRecipe {

    private final Ingredient machine;
    private final int huPerTick;
    private final List<Fuel> fuels;

    public SolidBurnerRecipe(Ingredient machine, int huPerTick, List<Fuel> fuels) {
        this.machine = machine;
        this.huPerTick = huPerTick;
        this.fuels = List.copyOf(fuels);
    }

    @Override
    public Ingredient machine() {
        return machine;
    }

    public int huPerTick() {
        return huPerTick;
    }

    public List<Fuel> fuels() {
        return fuels;
    }

    public Optional<Fuel> findFuel(ItemStack stack) {
        return fuels.stream().filter(fuel -> fuel.ingredient().test(stack)).findFirst();
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return machine.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(machine);
        fuels.forEach(fuel -> ingredients.add(fuel.ingredient()));
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SOLID_BURNER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SOLID_BURNER_TYPE.get();
    }

    /**
     * 单个燃料条目。
     */
    public record Fuel(Ingredient ingredient, int hu) {

        public static final Codec<Fuel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("fuel").forGetter(Fuel::ingredient),
                Codec.INT.fieldOf("hu").forGetter(Fuel::hu)
        ).apply(instance, Fuel::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Fuel> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, Fuel::ingredient,
                ByteBufCodecs.VAR_INT, Fuel::hu,
                Fuel::new);
    }

    public static class Serializer implements RecipeSerializer<SolidBurnerRecipe> {

        public static final MapCodec<SolidBurnerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("machine").forGetter(SolidBurnerRecipe::machine),
                Codec.INT.fieldOf("hu_per_tick").forGetter(SolidBurnerRecipe::huPerTick),
                Fuel.CODEC.listOf().fieldOf("fuels").forGetter(SolidBurnerRecipe::fuels)
        ).apply(instance, SolidBurnerRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SolidBurnerRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, SolidBurnerRecipe::machine,
                        ByteBufCodecs.VAR_INT, SolidBurnerRecipe::huPerTick,
                        Fuel.STREAM_CODEC.apply(ByteBufCodecs.list()), SolidBurnerRecipe::fuels,
                        SolidBurnerRecipe::new);

        @Override
        public MapCodec<SolidBurnerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SolidBurnerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
