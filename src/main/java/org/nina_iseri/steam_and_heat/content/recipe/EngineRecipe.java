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

import java.util.Locale;

/**
 * 引擎配方：引擎每 tick 需要多少 HU 或蒸汽，以及输出多少 RPM / SU。
 */
public class EngineRecipe implements ModRecipes.MachineRecipe {

    private final Ingredient machine;
    private final InputType input;
    private final int inputPerTick;
    private final float rpm;
    private final float su;
    private final double boilerEfficiency;

    public EngineRecipe(Ingredient machine, InputType input, int inputPerTick, float rpm, float su,
            double boilerEfficiency) {
        this.machine = machine;
        this.input = input;
        this.inputPerTick = inputPerTick;
        this.rpm = rpm;
        this.su = su;
        this.boilerEfficiency = boilerEfficiency;
    }

    @Override
    public Ingredient machine() {
        return machine;
    }

    public InputType input() {
        return input;
    }

    public int inputPerTick() {
        return inputPerTick;
    }

    public float rpm() {
        return rpm;
    }

    public float su() {
        return su;
    }

    public double boilerEfficiency() {
        return boilerEfficiency;
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
        return ModRecipes.ENGINE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ENGINE_TYPE.get();
    }

    /**
     * 引擎输入类型。
     */
    public enum InputType {
        HU("hu"),
        STEAM("steam");

        public static final Codec<InputType> CODEC =
                Codec.STRING.xmap(InputType::fromName, InputType::getName);
        public static final StreamCodec<io.netty.buffer.ByteBuf, InputType> STREAM_CODEC =
                ByteBufCodecs.STRING_UTF8.map(InputType::fromName, InputType::getName);

        private final String name;

        InputType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static InputType fromName(String name) {
            for (InputType value : values()) {
                if (value.name.equals(name.toLowerCase(Locale.ROOT))) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Unknown engine input type: " + name);
        }
    }

    public static class Serializer implements RecipeSerializer<EngineRecipe> {

        public static final MapCodec<EngineRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("machine").forGetter(EngineRecipe::machine),
                InputType.CODEC.fieldOf("input").forGetter(EngineRecipe::input),
                Codec.INT.fieldOf("input_per_tick").forGetter(EngineRecipe::inputPerTick),
                Codec.FLOAT.fieldOf("rpm").forGetter(EngineRecipe::rpm),
                Codec.FLOAT.fieldOf("su").forGetter(EngineRecipe::su),
                Codec.DOUBLE.fieldOf("boiler_efficiency").forGetter(EngineRecipe::boilerEfficiency)
        ).apply(instance, EngineRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, EngineRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, EngineRecipe::machine,
                        InputType.STREAM_CODEC, EngineRecipe::input,
                        ByteBufCodecs.VAR_INT, EngineRecipe::inputPerTick,
                        ByteBufCodecs.FLOAT, EngineRecipe::rpm,
                        ByteBufCodecs.FLOAT, EngineRecipe::su,
                        ByteBufCodecs.DOUBLE, EngineRecipe::boilerEfficiency,
                        EngineRecipe::new);

        @Override
        public MapCodec<EngineRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EngineRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
