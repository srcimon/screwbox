package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.*;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.FluidRenderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.AsciiMap;

public class PlaygroundScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(5);
    }

    @Override
    public void populate(Environment environment) {
        var map = AsciiMap.fromString("""
                
                
                
                
                          BB
                                   P           ####
                #########wwwwwww##########wwww#########
                #########wwwwwww###########################
                #########wwwwwww###########################
                #########wwwwwww###########################
                #########wwwwwww###########################
                #########wwwwwww###########################
                #################################################
                #################################################
                #################################################
                #################################################
                #################################################
                ###############################            ################
                """);

        environment.importSource(map)
                .as(world -> new Entity().name("world-info")
                        .add(new CameraBoundsComponent(world.bounds()))
                        .add(new GravityComponent(Vector.y(600))));

        environment.importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)

                .when('#').as(tile -> new Entity().name("ground")
                        .bounds(tile.bounds())
                        .add(new RenderComponent(Sprite.placeholder(Color.hex("#144c64"), tile.size())))
                        .add(new ColliderComponent())
                        .add(new StaticColliderComponent()))

                .when('P').as(tile -> new Entity().name("player")
                        .bounds(tile.bounds())
                        .add(new PhysicsComponent())
                        .add(new FluidInteractionComponent())
                        .add(new CollisionDetailsComponent())
                        .add(new CollisionSensorComponent())
                        .add(new AirFrictionComponent(250, 20))
                        .add(new JumpControlComponent())
                        .add(new SuspendJumpControlComponent())
                        .add(new CameraTargetComponent())
                        .add(new FloatComponent(400, 500))
                        .add(new FloatRotationComponent())
                        .add(new RenderComponent(Sprite.placeholder(Color.RED, tile.size())))
                        .add(new LeftRightControlComponent()));

        environment.importSource(map.blocks())
                .usingIndex(AsciiMap.Block::value)
                .when('w').as(block -> new Entity().name("water")
                        .bounds(block.bounds())
                        .add(new FluidComponent((int) block.bounds().width() / 8))
                        .add(new FluidRenderComponent()))

                .when('B').as(block -> new Entity().name("box")
                        .bounds(block.bounds())
                        .add(new SinkableComponent())
                        .add(new PhysicsComponent())
                        .add(new FluidInteractionComponent())
                        .add(new RenderComponent(Sprite.placeholder(Color.RED, block.size())))
                        .add(new FloatComponent())
                        .add(new ColliderComponent()));

        environment
                .enableAllFeatures()
                .addSystem(new SwitchSceneSystem())
                .addSystem(new SinkableSystem())
                .addSystem(new LogFpsSystem());
    }
}
