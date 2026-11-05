package org.nina_iseri.steam_and_heat.client;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

public class ParticleBase extends TextureSheetParticle {
    protected float originalScale = 1.3F;

    protected ParticleBase(ClientLevel world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public ParticleBase(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.xd*=1.25;
        this.yd*=1.25;
        this.zd*=1.25;
    }



    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer worldRendererIn, Camera entityIn, float pt) {
        float age = (this.age + pt) / lifetime * 32.0F;

        age = Mth.clamp(age, 0.0F, 1.0F);
        super.alpha=Mth.clamp(1-(this.age + pt) / lifetime, 0.0F, 1.0F);
        super.quadSize = originalScale*(age+this.age*0.01875F);
        super.render(worldRendererIn, entityIn, pt);
    }

    public void tick() {
        super.xo = x;
        super.yo = y;
        super.zo = z;
        if (age >= lifetime)
            super.remove();
        this.age++;
        this.yd -= 0.04D * gravity;
        move(xd,yd, zd);

        if (y == yo) {
            this.xd *= 1.1D;
            this.zd *= 1.1D;
            this.age+=7;
        }
        this.xd *= 0.96D;
        this.yd *= 0.96D;
        this.zd *= 0.96D;

        if (onGround) {
            this.xd *= 0.67D;
            this.zd *= 0.67D;
        }
    }


}
