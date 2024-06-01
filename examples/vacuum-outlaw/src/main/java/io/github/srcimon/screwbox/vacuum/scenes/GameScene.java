package io.github.srcimon.screwbox.vacuum.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.tiled.Map;
import io.github.srcimon.screwbox.vacuum.player.Player;
import io.github.srcimon.screwbox.vacuum.player.movement.DashSystem;
import io.github.srcimon.screwbox.vacuum.player.movement.MovementControlSystem;
import io.github.srcimon.screwbox.vacuum.tiles.DecorTile;
import io.github.srcimon.screwbox.vacuum.tiles.WallTile;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

public class GameScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
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
                .enablePhysics()
                .enableLight()
                .addSystem(new DashSystem())
                .addSystem(new MovementControlSystem())
                .addSystem(new LogFpsSystem())
                .enableTweening()
                .enableLogic()
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
                .when("player").as(new Player())
                //TODO .andAs(----)
                .when("light").as(object -> new Entity(object.id()).name("light")
                        .add(new TransformComponent(object.position()))
                        .add(new GlowComponent(18, Color.hex("#feffe9")))
                        .add(new PointLightComponent(120, Color.BLACK)));
        environment.importSource(map.tiles())
                .usingIndex(tile -> tile.layer().clazz())
                .when("wall").as(new WallTile())
                .when("decor").as(new DecorTile());
    }
}
