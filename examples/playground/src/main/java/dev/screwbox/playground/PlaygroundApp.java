package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.builder.RopeBuilder;
import dev.screwbox.playground.builder.SoftbodyBuilder;
import dev.screwbox.playground.misc.PhysicsInteractionSystem;

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
                WWWWWWWWWWWWWWWWWWWWWWWWWW
                WWWWWWWWWWWWWWWWWWWWWWWWWW
                """);

        Environment environment = engine.environment();
        environment
                .enableAllFeatures()
                .addSystem(new SoftBodyPreasureSystem())
//                .addSystem(new DebugJointsSystem())
                .addSystem(new PhysicsInteractionSystem())
                .addSystem(new LogFpsSystem())
                .addEntity(new Entity().add(new GravityComponent(Vector.y(400))));

        var xEntity = map.tiles().stream().filter(tile -> tile.value().equals('X')).findFirst().orElseThrow();
        RopeBuilder.createRope(environment, xEntity.bounds().position(), xEntity.bounds().position().add(10, 60), 8);

        environment.addEntity(new Entity()
                .bounds(Bounds.atOrigin(0, 0, 16, 16))
                .add(new CursorAttachmentComponent())
                .add(new TailwindComponent(40, Percent.max())));

        environment
                .addSystem(new DemoSystem())
                .importSource(map.blocks())
                .usingIndex(TileMap.Block::value)
                .when('W').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new FluidComponent(20))
                        .add(new FluidRenderComponent())
                        .add(new FluidTurbulenceComponent(), t -> t.strength = 700));

        environment.addSystem(Order.OPTIMIZATION, x -> {
            if (engine.mouse().isPressedRight()) {
                environment.addEntities(SoftbodyBuilder.create(engine.mouse().position(), environment));
               // environment.addEntities(SoftbodyBuilder.createBall(engine.mouse().position(), environment, 8));
            }
        });
        environment
             //   .addSystem(new DebugJointsSystem())
//                .addSystem(new DynamicCreationSystem())
                .addSystem(new SoftBodyCollisionSystem())
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