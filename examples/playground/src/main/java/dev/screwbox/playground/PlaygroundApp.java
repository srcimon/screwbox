package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.imports.ImportCondition;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.imports.ImportJob;
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
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyPressureComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.blueprints.HangingRope;
import dev.screwbox.playground.builder.BuilderSystem;
import dev.screwbox.playground.misc.DebugJointsSystem;
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

        engine.environment().runImport(ImportJob.indexedSource(map.tiles(), TileMap.Tile::value)
                .assign('X', new HangingRope())
                .assign('W', tile -> new Entity())
                .assign(ImportCondition.index('X'), tile -> new Entity()));

        Environment environment = engine.environment();
        environment
                .enableAllFeatures()
//                .addSystem(new DebugJointsSystem())
                .addSystem(new PhysicsInteractionSystem())
                .addEntity(new Entity().add(new GravityComponent(Vector.y(400))))
                .addSystem(new LogFpsSystem());

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

        environment.addSystem(new BuilderSystem());
        environment.addSystem(Order.OPTIMIZATION, x -> {

            if (engine.mouse().isPressedRight()) {
//                environment.addEntities(OldSoftbodyBuilder.create(engine.mouse().position(), environment));
                // environment.addEntities(SoftbodyBuilder.createBall(engine.mouse().position(), environment, 8));
            }
        });
        environment.addSystem(x -> {
            for (var body : x.environment().fetchAll(Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftBodyPressureComponent.class))) {
                if (x.keyboard().isDown(Key.T)) {
                    body.get(SoftBodyPressureComponent.class).pressure -= 200;
                }
                if (x.keyboard().isDown(Key.Z)) {
                    body.get(SoftBodyPressureComponent.class).pressure += 200;
                }
            }
        });
        environment
                .addSystem(new DebugJointsSystem())
//                .addSystem(new DynamicCreationSystem())
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