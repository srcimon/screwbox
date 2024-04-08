package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDebugSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleSystem;
import io.github.srcimon.screwbox.core.keyboard.Key;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;

//TODO fixup example
//TODO MAKE SMOKE SPRITE PART O BOUNDLE
public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.environment()
                .addSystem(new LogFpsSystem())
                .enablePhysics()
                .enableRendering()
                .enableTweening()
                .addSystem(engine -> {
                    if (engine.keyboard().isPressed(Key.ENTER)) {
                        engine.graphics().configuration().toggleFullscreen();
                    }
                })
                .addSystem(new ParticleSystem())
                .addSystem(engine -> engine.environment().fetchById(1).moveTo(engine.mouse().position()))
                .addSystem(engine -> {
                    if (engine.mouse().isPressedLeft()) {
                        engine.environment().toggleSystem(new ParticleDebugSystem());
                    }
                })
                .addEntity(1,
                        new TransformComponent(0, 0, 64, 64),
                        new ParticleEmitterComponent(withInterval(ofMillis(15))));

        screwBox.start();
    }
}