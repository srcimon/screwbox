package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.controls.JumpControlComponent;
import io.github.srcimon.screwbox.core.environment.controls.LeftRightControlComponent;
import io.github.srcimon.screwbox.core.environment.controls.SuspendJumpControlComponent;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.FluidRenderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.AsciiMap;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(2);

        var map = AsciiMap.fromString("""
                
                                   P           ####
                #########wwwwwww######################
                #########wwwwwww###########################
                #################################################
                ###############################            ################
                """);

        engine.environment().importSource(map)
                .as(world -> new Entity().name("world-info")
                        .add(new CameraBoundsComponent(world.bounds()))
                        .add(new GravityComponent(Vector.y(600))));

        engine.environment().importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('#').as(tile -> new Entity().name("ground")
                        .bounds(tile.bounds())
                        .add(new RenderComponent(Sprite.placeholder(Color.hex("#144c64"), tile.size())))
                        .add(new ColliderComponent())
                        .add(new StaticColliderComponent()))

                .when('P').as(tile -> new Entity().name("player")
                        .bounds(tile.bounds())
                        .add(new PhysicsComponent())
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

        engine.environment().importSource(map.chunks())
                .usingIndex(AsciiMap.Chunk::value)
                .when('w').as(chunk -> new Entity().name("water")
                        .bounds(chunk.bounds())
                        .add(new FluidComponent(32))
                        .add(new FluidRenderComponent()));

        engine.environment()
                .enableAllFeatures()
                .addSystem(new LogFpsSystem());

        engine.start();
    }
}