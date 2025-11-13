package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidEffectsComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.joint.Joint;
import dev.screwbox.playground.joint.JointComponent;
import dev.screwbox.playground.joint.JointsSystem;
import dev.screwbox.playground.rope.RopeComponent;
import dev.screwbox.playground.rope.RopeRenderComponent;
import dev.screwbox.playground.rope.RopeRenderSystem;
import dev.screwbox.playground.rope.RopeSystem;
import dev.screwbox.playground.softbody.SoftbodyBuilder;
import dev.screwbox.playground.softbody.SoftbodyRenderSystem;
import dev.screwbox.playground.softbody.SoftbodySystem;

import java.util.ArrayList;
import java.util.List;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().camera().setZoom(3);

        var map = TileMap.fromString("""
                
                
                
                      N          # X#
                     ##          ####
                       
                                c                
               
               
                #######
                ###   ####
                WWWWWWWWWWWWWWWWWWWWWWWWWW
                WWWWWWWWWWWWWWWWWWWWWWWWWW
                """);

        engine.environment()
                .enableAllFeatures()
//                .addSystem(new DebugJointsSystem())
                .addSystem(new SoftbodyRenderSystem())
                .addSystem(new SoftbodySystem())//TODO is same as rope system
                .addSystem(new RopeRenderSystem())
                .addSystem(new JointsSystem())
                .addSystem(new RopeSystem())
                .addSystem(new PhysicsInteractionSystem())
                .addSystem(new LogFpsSystem())
                .addEntity(new Entity().add(new GravityComponent(Vector.y(400))));

        var xEntity = map.tiles().stream().filter(tile -> tile.value().equals('X')).findFirst().orElseThrow();
        double dist = 0;
        int max = 8;
        for (int i = max; i >= 0; i--) {
            Entity add = new Entity(100 + i)
                    .name(i == 0 ? "start" : "node")
                    .add(new FloatComponent())
                    .bounds(xEntity.bounds().moveBy(0, dist).expand(-12))
                    .add(new PhysicsComponent(), p -> p.friction = 80);
            if (i == 0) {
                add.add(new RopeComponent());
                add.add(new RopeRenderComponent(Color.ORANGE, 4));
            }
            if (i != max) {
                add.add(new JointComponent(List.of(new Joint(100 + i + 1))));
            } else {

                add.add(new JointComponent(new ArrayList<>()));
            }
            engine.environment().addEntity(add);
            dist += 12;
        }

        engine.environment()
                .importSource(map.blocks())
                .usingIndex(TileMap.Block::value)
                .when('W').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new FluidComponent(20))
                        .add(new FluidRenderComponent())

                        .add(new FluidEffectsComponent())
                        .add(new FluidTurbulenceComponent()));

        engine.environment().addSystem(x -> {
            if (engine.mouse().isPressedRight()) {
                engine.environment().addEntities(SoftbodyBuilder.create(engine.mouse().position()));
            }
        });
        engine.environment()
                .importSource(map.tiles())
                .usingIndex(TileMap.Tile::value)

                .when('c').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new CameraTargetComponent()))

                .when('#').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new RenderComponent(tile.findSprite(AutoTileBundle.ROCKS)))
                        .add(new ColliderComponent())
                        .add(new StaticColliderComponent()));

        engine.start();
    }
}