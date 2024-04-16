package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.ParticleOptionsBundle;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import static io.github.srcimon.screwbox.core.assets.FontsBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.graphics.TextDrawOptions.font;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");
        Sheduler x = Sheduler.withInterval(Duration.ofMillis(50));
        Sheduler spawn = Sheduler.withInterval(Duration.ofMillis(50));
        screwBox.loop().unlockFps();

        screwBox.environment()
                .enableTweening()
                .addSystem(new LogFpsSystem())
                .addSystem(e -> {
                    e.environment().fetchAllHaving(ParticleInteractionComponent.class).forEach(
//                            entity -> entity.get(PhysicsComponent.class).momentum = e.keyboard().wsadMovement(500));
                            entity -> entity.moveTo(e.mouse().position()));
                })
                .addSystem(new ParticleInteractionSystem())
                .enableRendering()
                .addEntity(new PhysicsComponent(),
                        new RenderComponent(SpritesBundle.DOT_YELLOW_16),
                        new TransformComponent(-60, -60, 120, 120),
                        new ParticleInteractionComponent(80))
                .addSystem(engine -> engine.window().setTitle(""+engine.particles().particleCount()))
                .enablePhysics()
                        .addSystem(engine -> {
                        });

        screwBox.environment().addSystem(engine -> {
            if(spawn.isTick()) {
                engine.particles().spawnMultiple(6, engine.graphics().world().visibleArea(), ParticleOptionsBundle.CONFETTI.get()
                        .sprites(SpritesBundle.DOT_BLUE_16));
            }
        });

        screwBox.start();
    }
}