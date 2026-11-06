package org.nina_iseri.steam_and_heat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;

public class ModTags {

    public static void register() {

    }

    public static final TagKey<Fluid> STEAM = forgeTag("steam");

    private static TagKey<Fluid> forgeTag(String name) {
        return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

}
