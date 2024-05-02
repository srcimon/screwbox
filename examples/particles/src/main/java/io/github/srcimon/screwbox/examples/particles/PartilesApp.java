package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;

import static io.github.srcimon.screwbox.core.assets.ParticleOptionsBundle.CONFETTI;
import static io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent.SpawnMode.AREA;

public class PartilesApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles");

        screwBox.environment()
                .enableTweening()
                .enableParticles()
                .enablePhysics()
                .addSystem(engine -> {
                    if(engine.keyboard().isPressed(Key.SPACE)) {
                        engine.scenes().add(new AltScene());
                        engine.scenes().switchTo(AltScene.class, SceneTransition
                                        .extro(new SceneTransition.ExtroAnimation() {
                                            @Override
                                            public void draw(Screen screen, Percent progress) {
                                                screen.fillWith(Color.BLUE.opacity(progress));
                                            }
                                        }, Duration.ofSeconds(2)));
                    }
                })
                .addEntity("particle spawner",
                        new TransformComponent(screwBox.graphics().world().visibleArea()),
                        new ParticleEmitterComponent(Duration.ofMillis(10), AREA, CONFETTI))
                .addSystem(new LogFpsSystem())
                .addEntity(1, "particle interactor",
                        new PhysicsComponent(),
                        new TransformComponent(-60, -60, 120, 120),
                        new ParticleInteractionComponent(80))
                .addSystem(engine -> engine.environment().fetchById(1).moveTo(engine.mouse().position()))
                .enableRendering();

        screwBox.start();
    }
}