package io.github.srcimon.screwbox.vacuum;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.light.SpotLightComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.tiled.Map;

public class GameScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(6);
        engine.graphics().light().setAmbientLight(Percent.of(0.2));
        engine.window()
                .setCursor(Sprite.fromFile("cursor.png").scaled(4));
    }

    @Override
    public void populate(Environment environment) {
        Map map = Map.fromJson("untitled.json");

        environment
                .addSystem(engine -> {
                    Entity player = engine.environment().fetchById(1);
                    SpeedComponent speedComponent = player.get(SpeedComponent.class);
                    if(engine.keyboard().isPressed(Key.SPACE)) {
                        speedComponent.speed = 400;
                    }

                    speedComponent.speed = Math.max(150, speedComponent.speed - engine.loop().delta(800));

                    player.get(PhysicsComponent.class).momentum = engine.keyboard().wsadMovement(speedComponent.speed);
                })//TODO: FIXUP
                .addSystem(engine -> {
                    if(engine.mouse().isPressedLeft()) {
                        Entity player = engine.environment().fetchById(1);
                        engine.audio().playSound(SoundBundle.PHASER);
                        var speed = engine.mouse().position().substract(player.position()).length(200);
                        engine.environment().addEntity(new Entity("bullet")
                                .add(new TransformComponent(player.position(), 8, 8))
                                        .add(new GlowComponent(10, Color.YELLOW.opacity(0.4)))
                                        .add(new TweenComponent(Duration.oneSecond()))
                                        .add(new TweenDestroyComponent())
                                        .add(new PointLightComponent(40, Color.BLACK))
                                .add(new RenderComponent(SpriteBundle.ELECTRICITY_SPARCLE, player.get(RenderComponent.class).drawOrder, SpriteDrawOptions.scaled(0.5)))
                                .add(new PhysicsComponent(speed))
                        );
                    }
                })
                .addSystem(new AttachmentSystem())
                .enablePhysics()
                .enableLight()
                .enableTweening()
                .enableRendering();

        environment.addEntity(new Entity("player shadow").add(new TransformComponent(0,0, 4, 2)).add(new ShadowCasterComponent()).add(new AttachmentComponent(1, Vector.of(0,6))));
        environment.importSource(map.objects())
                .usingIndex(object -> object.name())
                .when("player").as(object -> new Entity(object.id()).name("player")
                        .add(new TransformComponent(object.position(), 12, 8))
                        .add(new PhysicsComponent())
                        .add(new SpeedComponent())
                        .add(new RenderComponent(SpriteBundle.MAN_STAND, object.layer().order()))
                        .add(new CameraTargetComponent(40)))
                //TODO .andAs(----)

                .when("light").as(object -> new Entity(object.id()).name("light")
                        .add(new TransformComponent(object.position()))
                        .add(new PointLightComponent(120, Color.BLACK))
                        .add(new GlowComponent(100, Color.YELLOW.opacity(0.1))));

        environment.importSource(map.tiles())
                .usingIndex(tile -> tile.layer().name())
                .when("walls").as(tile -> new Entity().name("wall")
                        .add(new ColliderComponent())
                        .add(new ShadowCasterComponent())
                        .add(new RenderComponent(tile.sprite(), tile.layer().order()))
                        .add(new TransformComponent(tile.renderBounds())))
                .stopUsingIndex()//TODO: usingIndex
                .usingIndex(tile -> tile.layer().name())
                .when("decoration").as(tile -> new Entity().name("decoration")
                        .add(new RenderComponent(tile.sprite(), tile.layer().order()))
                        .add(new TransformComponent(tile.renderBounds())))
                .when("decorationover").as(tile -> new Entity().name("decoration")
                        .addCustomized(new RenderComponent(tile.sprite(), tile.layer().order()),
                                renderComponent -> {
                            renderComponent.renderOverLight = true;
                            renderComponent.parallaxX = tile.layer().parallaxX();
                            renderComponent.parallaxY= tile.layer().parallaxY();
                                }
                        )
                        .add(new TransformComponent(tile.renderBounds())))
                .stopUsingIndex()
                .usingIndex(tile -> tile.layer().name())
                .when("shadows").as(tile -> new Entity().name("floor")
                        .add(new RenderComponent(tile.sprite(), tile.layer().order()))
                        .add(new TransformComponent(tile.renderBounds())));
    }
}
