package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.ParticleOptionsBundle;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;

public class ParticalsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");
        Sheduler spawn = Sheduler.withInterval(Duration.ofMillis(50));
        screwBox.environment()
                .enableTweening()
                .enableParticles()
                .enablePhysics()
                .addSystem(new LogFpsSystem())
                .addSystem(e -> e.environment().fetchAllHaving(ParticleInteractionComponent.class).forEach(
                        entity -> entity.moveTo(e.mouse().position())))
                .enableRendering()
                .addEntity(new PhysicsComponent(),
                        new RenderComponent(SpritesBundle.DOT_YELLOW_16),
                        new TransformComponent(-60, -60, 120, 120),
                        new ParticleInteractionComponent(80));


        screwBox.environment().addSystem(engine -> {
            if (spawn.isTick()) {
                engine.particles().spawnMultiple(3, engine.graphics().world().visibleArea(), ParticleOptionsBundle.CONFETTI.get()
                        .sprites(SpritesBundle.DOT_BLUE_16)

                );
            }
        });

        screwBox.start();
    }
}