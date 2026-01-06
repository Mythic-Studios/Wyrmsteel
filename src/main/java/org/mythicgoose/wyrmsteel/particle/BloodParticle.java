package org.mythicgoose.wyrmsteel.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class BloodParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected BloodParticle(ClientLevel clientLevel, double x, double y, double z,
                            SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);

        // Random scale for variety
        this.quadSize = 0.1f + this.random.nextFloat() * 0.05f;

        // Set lifetime (1-2 seconds at 20 ticks/second)
        this.lifetime = 20 + this.random.nextInt(20);

        // Gravity effect
        this.gravity = 0.3f;

        // Set color (red for blood)
        this.rCol = 0.6f + this.random.nextFloat() * 0.2f;
        this.gCol = 0.0f;
        this.bCol = 0.0f;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);

        // Store previous position
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        // Check if particle should be removed
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        // Apply gravity
        this.yd -= 0.04 * this.gravity;

        // Move the particle
        this.move(this.xd, this.yd, this.zd);

        // Slow down over time (friction)
        this.xd *= 0.98;
        this.yd *= 0.98;
        this.zd *= 0.98;

        // Stop if on ground
        if (this.onGround) {
            this.xd *= 0.7;
            this.zd *= 0.7;
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new BloodParticle(level, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}