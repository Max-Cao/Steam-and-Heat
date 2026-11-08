package org.nina_iseri.steam_and_heat.client;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SteamParticle extends ModParticleBase {
    public SteamParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY,
            double motionZ) {
        super(world, x, y, z, motionX, motionY + 0.4f, motionZ);
        this.gravity = -0.05F;
        this.rCol = this.gCol = this.bCol = 1.0f;
        this.originalScale = 0.6F;
        this.lifetime = (int) (160.0D / (Math.random() * 0.2D + 0.8D));
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed) {
            SteamParticle steamParticle = new SteamParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            steamParticle.pickSprite(this.spriteSet);
            return steamParticle;
        }
    }
}
