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
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.builder.RopeBuilder;
import dev.screwbox.playground.builder.SoftbodyBuilder;
import dev.screwbox.playground.misc.PhysicsInteractionSystem;

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

        Environment environment = engine.environment();
        final List<Vector> points = new ArrayList<>();
        environment.addSystem(Order.OPTIMIZATION, x -> {
            if (engine.mouse().isPressedRight()) {
                points.add(engine.mouse().position());
            }
            if (engine.keyboard().isPressed(Key.SPACE)) {
                int firstId = environment.peekId();
                final var entities = new ArrayList<Entity>();
                for (int i = 0; i < points.size(); i++) {
                    var p = points.get(i);
                    entities.add(new Entity(environment.allocateId())
                            .add(i == points.size() - 1 ? new SoftLinkComponent(firstId) : new SoftLinkComponent(environment.peekId()))
                            .add(new TransformComponent(p, 2, 2))
                            .add(new PhysicsComponent(), y -> {
                                y.friction = 2;
                            })
                            .add(new FloatComponent())
                    );
                }
                entities.get(0)
                        .add(new SoftBodyRenderComponent(Color.ORANGE.opacity(0.5)), xx -> xx.outlineColor = Color.ORANGE)
                        .add(new SoftBodyComponent())
                        .add(new SoftbodyCollisionComponent());


                for (int i = 0; i < entities.size(); i++) {
                    int index = (i + 8) % entities.size();
                    int index2 = (i + 3) % entities.size();
                    int i1 = entities.get(index).id().get();
                    List<Integer> linked = new ArrayList<>();

                    if (i1 != entities.get(i).id().get()) {
                        linked.add(i1);
                    }
                    Integer i2 = entities.get(index2).id().get();
                    if (i2 != entities.get(i).id().get()) {
                        linked.add(i2);
                    }
                    entities.get(i).add(new SoftStructureComponent( linked));
                }
                environment.addEntities(entities);
                points.clear();
            }
        });
        environment
                .enableAllFeatures()
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
                .importSource(map.blocks())
                .usingIndex(TileMap.Block::value)
                .when('W').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new FluidComponent(20))
                        .add(new FluidRenderComponent())
                        .add(new FluidTurbulenceComponent(), t -> t.strength = 700));

//        environment.addSystem(Order.OPTIMIZATION, x -> {
//            if (engine.mouse().isPressedRight()) {
//                environment.addEntities(SoftbodyBuilder.create(engine.mouse().position(), environment));
//            }
//        });
        environment
//                .addSystem(new DebugJointsSystem())
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