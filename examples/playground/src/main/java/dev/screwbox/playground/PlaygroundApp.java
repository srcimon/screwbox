package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlSystem;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.DirectionalLightComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.TileMap;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().light().setAmbientLight(Percent.half());
        engine.graphics().camera()
            .move($(40, 40))
            .setZoom(4);
        engine.loop().unlockFps();
        engine.graphics().configuration().setLightQuality(Percent.half());
        var map = TileMap.fromString("""
               O   O
            P  # ###    ##
            #   RRR## O
              T       O  ##
            ###    ######
            """);
        engine.environment()
            .enableAllFeatures()
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', tile -> new Entity()
                    .bounds(tile.bounds())
                    .add(new StaticOccluderComponent())
                    .add(new OccluderComponent())
                    .add(new ColliderComponent())
                    .add(new StaticColliderComponent())
                    .add(new RenderComponent(Sprite.placeholder(Color.DARK_GREEN, 16)))
                )
                .assign('O', tile -> new Entity()
                    .bounds(tile.bounds())
                    .add(new StaticOccluderComponent())
                    .add(new OccluderComponent(false))
                    .add(new ColliderComponent())
                    .add(new StaticColliderComponent())
                    .add(new RenderComponent(Sprite.placeholder(Color.GREY, 16)))
                )
                .assignComplex('T', (tile, idPool) -> {
                    var body = SoftPhysicsSupport.createSoftBody(tile.bounds().expand(-2), idPool);
                    body.root().add(new SoftBodyRenderComponent(Color.ORANGE.opacity(0.5)), r -> {
                        r.outlineStrokeWidth = 2;
                        r.outlineColor = Color.ORANGE;
                    });
                    body.forEach(node -> node.get(PhysicsComponent.class).friction =2);
                    body.forEach(node -> node.add(new LeftRightControlComponent()));
                    body.forEach(node -> node.add(new JumpControlComponent()));
                    body.forEach(node -> node.add(new SuspendJumpControlComponent(), j->j.maxJumps = 2));
                    return body;
                })
                .assignComplex('R', (tile, idPool) -> {
                    var rope = SoftPhysicsSupport.createRope(tile.position().addY(tile.size().height() / -2.0), tile.position().addY(20), 8, idPool);
                    rope.forEach(node -> node.get(PhysicsComponent.class).friction = 2);
                    rope.forEach(node -> node.add(new ChaoticMovementComponent(400, Duration.ofMillis(800))));
                    rope.root()
                        .add(new RopeRenderComponent(Color.WHITE, 4))
                        .remove(PhysicsComponent.class);
                    return rope;
                })
                .assign('P', tile -> new Entity().bounds(tile.bounds().expand(-8))
                    .add(new StaticOccluderComponent())
                    .add(new OccluderComponent(false))
                    .add(new PhysicsComponent(), p -> p.friction = 3)
                    .add(new LeftRightControlComponent())
                    .add(new JumpControlComponent(), j -> j.acceleration = 300)
                    .add(new CollisionSensorComponent())
                    .add(new CollisionDetailsComponent())
                    .add(new SuspendJumpControlComponent(), s -> s.maxJumps = 2)
                    .add(new RenderComponent(Sprite.placeholder(Color.YELLOW, 8)))))
            .addEntity(new Entity().add(new GravityComponent(Vector.y(500))))
            .addSystem(new LogFpsSystem())
            .addEntity(new Entity().add(new TransformComponent()).add(new CursorAttachmentComponent()).add(new PointLightComponent(80, Color.BLACK)))
//            .addEntity(new Entity().bounds(map.bounds().expand(1000)).add(new DirectionalLightComponent(), d -> d.angle = Angle.degrees(-10)))
            .addSystem(e -> e.environment().tryFetchSingletonComponent(DirectionalLightComponent.class).ifPresent(d -> d.angle = Angle.degrees(e.mouse().position().x() / 4)))
            .addSystem(e -> e.graphics().canvas().fillWith(Color.BLUE))
            .addSystem(e -> {
                e.environment().fetchAllHaving(RopeRenderComponent.class).forEach(rope -> {
                    Polygon shape = rope.get(RopeComponent.class).shape;
                    List<Vector> thickened = new ArrayList<>();
                    thickened.addAll(shape.nodes());
                    int strokeWidth = rope.get(RopeRenderComponent.class).strokeWidth;
                    shape.nodes().reversed().forEach(node -> thickened.add(node.add(strokeWidth, strokeWidth)));//TODO fix this shitty workaround
                    thickened.add(shape.firstNode());

                    e.graphics().light().addBackgdropOccluder(Polygon.ofNodes(thickened), 0.75);
                    //TODO shape, strokeWidth, opacity
                    //TODO shape, strokeWidth, opacity, contentOpacity
                });
            })
            .addSystem(e -> {
                e.environment().fetchAllHaving(SoftBodyRenderComponent.class).forEach(rope -> {
                    Polygon shape = rope.get(SoftBodyComponent.class).shape;

                    e.graphics().light().addBackgdropOccluder(shape, 0.75);
                    //TODO shape, strokeWidth, opacity
                    //TODO shape, strokeWidth, opacity, contentOpacity
                });
            });
        engine.start();
    }

}