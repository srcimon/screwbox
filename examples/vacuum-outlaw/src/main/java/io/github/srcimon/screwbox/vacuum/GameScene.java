package io.github.srcimon.screwbox.vacuum;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.light.StaticShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.physics.StaticColliderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.particles.ParticleOptionsBundle;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.tiled.Map;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

public class GameScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
//        engine.graphics().configuration().lightFalloff(Percent.of(0.5));
        engine.graphics().camera().setZoom(3.5);
        engine.graphics().light().setAmbientLight(Percent.of(0.2));
    }

    @Override
    public void populate(Environment environment) {
        Map map = Map.fromJson("maps/DemoLevel.json");

        environment.addSystem(engine -> {
            if (engine.keyboard().isPressed(Key.K)) {
                engine.graphics().configuration().toggleFullscreen();
            }
        });
        environment
                .addSystem(engine -> {
                    Entity player = engine.environment().fetchById(5);
                    engine.graphics().light().addTopLightShadowCaster(player.bounds());
                    SpeedComponent speedComponent = player.get(SpeedComponent.class);
                    if (engine.keyboard().isPressed(Key.SPACE)) {
                        speedComponent.speed = 300;
                    }

                    speedComponent.speed = Math.max(80, speedComponent.speed - engine.loop().delta(800));

                    player.get(PhysicsComponent.class).momentum = engine.keyboard().wsadMovement(speedComponent.speed);
                })//TODO: FIXUP
                .enablePhysics()
                .enableLight()
                .addSystem(new LogFpsSystem())
                .enableTweening()
                .enableParticles()
                .enableRendering();

        environment.importSource(map)
                        .as(tiledMap -> new Entity("world").add(new CameraBoundsComponent(tiledMap.bounds())));

        environment.importSource(map.objects())
                .usingIndex(GameObject::name)
                .when("smoke").as(object -> new Entity(object.id()).name("smoke")
                        .add(new TransformComponent(object.bounds()))
                        .add(new ParticleEmitterComponent(Duration.ofMillis(200), ParticleOptions.unknownSource()
                                .sprite(SpriteBundle.ELECTRICITY_SPARCLE)
                                .drawOrder(object.layer().order())
                                .ease(Ease.SINE_IN_OUT)
                                .randomStartScale(1, 2)
                                .startOpacity(Percent.zero())
                                .animateOpacity(Percent.zero(), Percent.of(0.1))
                                .chaoticMovement(50, ofSeconds(1))
                                .randomStartRotation()
                                .lifetimeSeconds(2))))
                .when("player").as(object -> new Entity(object.id()).name("player")
                        .add(new TransformComponent(object.position(), 12, 8))
                        .add(new PhysicsComponent())
                        .add(new SpeedComponent())
                        .add(new RenderComponent(SpriteBundle.SLIME_MOVING, object.layer().order()))
                        .add(new CameraTargetComponent(5)))
                //TODO .andAs(----)
                .when("light").as(object -> new Entity(object.id()).name("light")
                        .add(new TransformComponent(object.position()))
                        .add(new GlowComponent(20, Color.WHITE.opacity(0.8)))
                        .add(new PointLightComponent(120, Color.BLACK)));

        environment.importSource(map.tiles())
                .usingIndex(tile -> tile.layer().clazz())
                .when("wall").as(tile -> new Entity().name("wall")
                        .add(new ColliderComponent())
                        .add(new ShadowCasterComponent())
                        .add(new StaticColliderComponent())
                        .add(new StaticShadowCasterComponent())
                        .add(new RenderComponent(tile.sprite(), tile.layer().order()))
                        .add(new TransformComponent(tile.renderBounds())))
                .when("decor").as(tile -> new Entity().name("decoration")
                        .addCustomized(new RenderComponent(tile.sprite(), tile.layer().order()),
                                renderComponent -> {
                                    renderComponent.parallaxX = tile.layer().parallaxX();
                                    renderComponent.parallaxY = tile.layer().parallaxY();
                                }
                        )
                        .add(new TransformComponent(tile.renderBounds())));
    }
}
