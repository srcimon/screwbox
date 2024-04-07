package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleSystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;
//TODO fixup example
public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.environment()
                .enablePhysics()
                .enableRendering()
                .enableTweening()
                .addSystem(new ParticleSystem())
                .addSystem(engine -> engine.environment().fetchById(1).moveTo(engine.mouse().position()))
                .addSystem(engine -> {
                    for (Entity entity : engine.environment().fetchAllHaving(TransformComponent.class)) {
                        engine.graphics().world().drawRectangle(entity.bounds(), RectangleDrawOptions.outline(Color.RED));
                    }
                })
                .addEntity(1,
                        new TransformComponent(0, 0, 40, 40),
                        new ParticleEmitterComponent(withInterval(ofMillis(10))));

        screwBox.start();
    }
}