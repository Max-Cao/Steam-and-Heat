// package org.nina_iseri.steam_and_heat.client.recipe;

// import java.util.Locale;
// import java.util.function.Supplier;

// import org.nina_iseri.steam_and_heat.SteamAndHeat;

// import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
// import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

// import net.minecraft.core.registries.BuiltInRegistries;
// import net.minecraft.core.registries.Registries;
// import net.minecraft.resources.ResourceLocation;
// import net.minecraft.world.item.crafting.Recipe;
// import net.minecraft.world.item.crafting.RecipeInput;
// import net.minecraft.world.item.crafting.RecipeSerializer;
// import net.minecraft.world.item.crafting.RecipeType;
// import net.neoforged.bus.api.IEventBus;
// import net.neoforged.neoforge.registries.DeferredHolder;
// import net.neoforged.neoforge.registries.DeferredRegister;

// public enum ModRecipes implements IRecipeTypeInfo {
//     SOLID_BURNER(SolidBurner::new);

//     public final ResourceLocation id;

//     public final Supplier<RecipeSerializer<?>> serializerSupplier;

//     private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;2

//     private final DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
//     private final Supplier<RecipeType<?>> type;

//     // 3. Constructor for Standard Processing Recipes (Matches AllRecipeTypes line
//     // 120)
//     ModRecipes(StandardProcessingRecipe.Factory<?> processingFactory) {
//         this(() -> new StandardProcessingRecipe.Serializer<>(processingFactory));
//     }
//     // 4. Main Constructor (Matches AllRecipeTypes line 109)

//     ModRecipes(Supplier<RecipeSerializer<?>> serializerSupplier) {
//         String name = name().toLowerCase(Locale.ROOT);

//     this.id = SteamAndHeat.resource(name); 
//     this.serializerSupplier = serializerSupplier;

//     this.serializerObject = Registers. SERIALIZER_REGISTER. register(name, serializerSupplier);

//     this.typeObject = Registers.RECIPE_TYPE_REGISTER. register(name, O-> RecipeType. simple(id)); this. type typeobject;
//     }

//     public static void register(IEventBus modEvent) {
//         Registers.RECIPE_TYPE_REGISTER.register(modEvent);
//         Registers.SERIALIZER_REGISTER.register(modEvent);
//     }

//     private static class Registers {
//         private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister
//                 .create(BuiltInRegistries.RECIPE_SERIALIZER, SteamAndHeat.MODID);

//         private static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister
//                 .create(Registries.RECIPE_TYPE, SteamAndHeat.MODID);
//     }

//     @Override
//     public ResourceLocation getId() {
//         return id;
//     }

//     @Override
//     public <T extends RecipeSerializer<?>> T getSerializer() {
//         return (T) serializerObject.get();
//     }

//     @Override
//     public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
//         return (RecipeType<R>) type.get();
//     }
// }
