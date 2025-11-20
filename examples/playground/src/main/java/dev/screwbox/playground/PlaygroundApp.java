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
import dev.screwbox.core.environment.fluids.FluidEffectsComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.particles.ParticleInteractionComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.joint.JointsSystem;
import dev.screwbox.playground.rope.RopeBuilder;
import dev.screwbox.playground.rope.RopeRenderSystem;
import dev.screwbox.playground.rope.RopeSystem;
import dev.screwbox.playground.softbody.SoftbodyBuilder;
import dev.screwbox.playground.softbody.SoftbodyRenderSystem;
import dev.screwbox.playground.softbody.SoftbodySystem;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().light().setAmbientLight(Percent.of(0.3));
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
        environment
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
        Bounds bounds = xEntity.bounds();
        RopeBuilder.createRope(environment, bounds);

        environment.addEntity(new Entity()
                .bounds(Bounds.atOrigin(0, 0, 16, 16))
                .add(new CursorAttachmentComponent())
                .add(new ParticleInteractionComponent(40, Percent.max())));

        environment
                .importSource(map.blocks())
                .usingIndex(TileMap.Block::value)
                .when('W').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new FluidComponent(20))
                        .add(new FluidRenderComponent())
                        .add(new FluidEffectsComponent())
                        .add(new FluidTurbulenceComponent()));

        environment.addSystem(x -> {
            if (engine.mouse().isPressedRight()) {
                environment.addEntities(SoftbodyBuilder.create(engine.mouse().position(), environment));
            }
        });
        environment
                .addSystem(Order.PRESENTATION_BACKGROUND, e -> engine.graphics().canvas().fillWith(Color.hex("#2c0707")))
                .addSystem(new LinkConeLightSystem())
                .importSource(map.tiles())
                .usingIndex(TileMap.Tile::value)

                .when('c').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new CameraTargetComponent()))

                .when('#').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new RenderComponent(tile.findSprite(AutoTileBundle.ROCKS)))
                        .add(new StaticOccluderComponent())
                        .add(new OccluderComponent())
                        .add(new ColliderComponent())
                        .add(new StaticColliderComponent()));

        engine.start();
    }

}