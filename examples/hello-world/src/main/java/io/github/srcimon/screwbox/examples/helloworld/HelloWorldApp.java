package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDebugSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.util.Optional;

import static io.github.srcimon.screwbox.core.Duration.between;
import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.assets.SpritesBundle.BLOB_ANIMATED_16;
import static io.github.srcimon.screwbox.core.assets.SpritesBundle.MOON_SURFACE_16;
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
                    if(engine.keyboard().isPressed(Key.ENTER)) {
                        engine.graphics().configuration().toggleFullscreen();
                    }
                })
                .addSystem(new ParticleSystem())
                .addSystem(engine -> engine.environment().fetchById(1).moveTo(engine.mouse().position()))
                .addSystem(engine -> {
                    if(engine.mouse().isPressedLeft()) {
                        engine.environment().toggleSystem(new ParticleDebugSystem());
                    }
                })
                .addSystem(engine -> {
//                    for (Entity entity : engine.environment().fetchAllHaving(RenderComponent.class)) {
//                        var render = entity.get(RenderComponent.class);
//                        engine.graphics().world().drawRectangle(entity.bounds().expand(render.sprite.size().width() * 4), RectangleDrawOptions
//                                .outline(Color.RED).
//                                rotation(render.options.rotation()));
//                    }
                })

                .addEntity(1,
                        new TransformComponent(0, 0, 64, 64),
                        new ParticleEmitterComponent(withInterval(ofMillis(15))));

        screwBox.start();
    }
}