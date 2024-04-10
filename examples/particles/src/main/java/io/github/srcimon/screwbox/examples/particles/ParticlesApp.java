package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleCustomizeChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleCustomizeRenderComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleCustomizeTweenComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDebugSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;

public class ParticlesApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles");

        screwBox.environment()
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero(), 128, 128),
                        new ParticleCustomizeRenderComponent(SpritesBundle.BLOB_ANIMATED_16, SpriteDrawOptions.scaled(3).opacity(Percent.of(0.8))),
                        new ParticleCustomizeChaoticMovementComponent(50, Duration.ofSeconds(1), Vector.y(-100)),
                        new ParticleCustomizeTweenComponent(Duration.ofSeconds(2), Percent.half()),
                        new ParticleEmitterComponent(withInterval(ofMillis(20))))
                .addSystem(new ParticleDebugSystem())
                .addSystem(new LogFpsSystem())
                .enableRendering()
                .enablePhysics()
                .enableTweening()
                .enableParticles();

        screwBox.start();
    }
}