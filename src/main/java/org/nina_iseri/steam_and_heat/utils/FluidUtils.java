package org.nina_iseri.steam_and_heat.utils;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public final class FluidUtils {
    public static FluidTank single_fluid_tank(int capacity, Fluid f) {
        return new FluidTank(capacity, s -> s.getFluid().equals(f));
    }
}
