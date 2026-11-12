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

/**
 * 锅炉配方：每 tick 消耗多少 HU、多少水，并输出多少蒸汽。
 */
public class BoilerRecipe implements ModRecipes.MachineRecipe {

    private final Ingredient machine;
    private final int huPerTick;
    private final int waterPerTick;
    private final int steamPerTick;

    public BoilerRecipe(Ingredient machine, int huPerTick, int waterPerTick, int steamPerTick) {
        this.machine = machine;
        this.huPerTick = huPerTick;
        this.waterPerTick = waterPerTick;
        this.steamPerTick = steamPerTick;
    }

    @Override
    public Ingredient machine() {
        return machine;
    }

    public int huPerTick() {
        return huPerTick;
    }

    public int waterPerTick() {
        return waterPerTick;
    }

    public int steamPerTick() {
        return steamPerTick;
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
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BOILER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BOILER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<BoilerRecipe> {

        public static final MapCodec<BoilerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("machine").forGetter(BoilerRecipe::machine),
                Codec.INT.fieldOf("hu_per_tick").forGetter(BoilerRecipe::huPerTick),
                Codec.INT.fieldOf("water_per_tick").forGetter(BoilerRecipe::waterPerTick),
                Codec.INT.fieldOf("steam_per_tick").forGetter(BoilerRecipe::steamPerTick)
        ).apply(instance, BoilerRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BoilerRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, BoilerRecipe::machine,
                        ByteBufCodecs.VAR_INT, BoilerRecipe::huPerTick,
                        ByteBufCodecs.VAR_INT, BoilerRecipe::waterPerTick,
                        ByteBufCodecs.VAR_INT, BoilerRecipe::steamPerTick,
                        BoilerRecipe::new);

        @Override
        public MapCodec<BoilerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BoilerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
