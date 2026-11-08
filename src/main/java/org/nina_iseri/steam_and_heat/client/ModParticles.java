package org.nina_iseri.steam_and_heat.client;

import org.nina_iseri.steam_and_heat.SteamAndHeat;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.particles.ParticleType;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister
            .create(BuiltInRegistries.PARTICLE_TYPE, SteamAndHeat.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STEAM = REGISTER.register("steam",
            () -> new SimpleParticleType(false));

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
