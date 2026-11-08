package org.nina_iseri.steam_and_heat.registrate;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;

import org.joml.Vector3f;

import java.util.function.Supplier;

import static org.nina_iseri.steam_and_heat.SteamAndHeat.REGISTRATE;

public class ModFluids {
    public static final FluidEntry<BaseFlowingFluid.Flowing> STEAM = REGISTRATE
            .standardFluid("steam", ModFluids.SolidRenderedPlaceableFluidType.create(0xDEDEDE, () -> 1f / 8f))
            .lang("Steam")
            .properties(b -> b.density(-10).viscosity(1).temperature(473).canPushEntity(false)
                    .canConvertToSource(false).canDrown(true))
            .fluidProperties(b -> b.slopeFindDistance(3).explosionResistance(100F))
            .tag(ModTags.STEAM)
            .source(BaseFlowingFluid.Source::new)
            .bucket()
            .tag(TagKey.create(BuiltInRegistries.ITEM.key(), AllTags.NameSpace.COMMON.id("buckets/steam")))
            .build()
            .register();


    // From Create's AllFluids.java
    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                ModFluids.SolidRenderedPlaceableFluidType fluidType = new ModFluids.SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        /*
         * Removing alpha from tint prevents optifine from forcibly applying biome
         * colors to modded fluids (this workaround only works for fluids in the solid
         * render layer)
         */
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }

    public static void register() {
    }
}
