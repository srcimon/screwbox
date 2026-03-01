package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.BackdropOccluderComponent;
import dev.screwbox.core.environment.light.DirectionalLightComponent;
import dev.screwbox.core.environment.light.GlowComponent;
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
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.softphysics.RopeOccluderComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyOccluderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SplitScreenOptions;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.ShockwaveOptions;
import dev.screwbox.core.graphics.postfilter.FishEyePostFilter;
import dev.screwbox.core.graphics.postfilter.UnderwaterPostFilter;
import dev.screwbox.core.graphics.options.ShadowOptions;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.misc.InteractionSystem;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().light().setAmbientLight(Percent.half());
        engine.graphics().camera()
            .move($(40, 40))
            .setZoom(4);
        engine.loop().unlockFps();
        engine.graphics().screen().setFlippedHorizontal(false).setFlippedVertical(true);
        engine.graphics().screen().setRotation(Angle.degrees(20));
        engine.graphics().enableSplitScreenMode(SplitScreenOptions.viewports(4).tableLayout());
        engine.graphics().configuration().setLightQuality(Percent.half());
        var map = TileMap.fromString("""
               O   O
            P  # ###    ##
            #   RRR## O
              T       O  ##
            
            
            
            
            ############    ######
            """);
        engine.environment()
            .enableAllFeatures()
            .addSystem(e -> {
                if (e.mouse().isPressedLeft()) {
                    e.graphics().postProcessing()
                        .clearFilters();
                } else if (e.mouse().isPressedRight()) {
                    e.graphics().postProcessing()
                        .triggerShockwave(e.mouse().position(), new ShockwaveOptions(160, 100, 80))
//                        .addFilter(new UnderwaterPostFilter())
                        .addFilter(new FishEyePostFilter(20, -0.2))
//                        .addViewportFilter(new FishEyePostFilter(20, -0.1))
//                        .addViewportFilter(new WarpPostFilter(Percent.of(0.5)))
                    ;
                }
            })
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
                    body.root().add(new SoftBodyRenderComponent(Color.ORANGE.opacity(0.5)), r -> r.outlineColor = Color.ORANGE);
                    body.root().add(new CameraTargetComponent(2, 40));
                    body.root().add(new SoftBodyOccluderComponent(ShadowOptions.rounded().backdropDistance(0.4).distortion(Percent.of(0.04))));
                    body.forEach(node -> node.get(PhysicsComponent.class).friction = 2);
                    body.forEach(node -> node.add(new LeftRightControlComponent()));
                    body.forEach(node -> node.add(new JumpControlComponent()));
                    return body;
                })
                .assignComplex('R', (tile, idPool) -> {
                    var rope = SoftPhysicsSupport.createRope(tile.position().addY(tile.size().height() / -2.0), tile.position().addY(20), 9, idPool);
                    rope.forEach(node -> node.get(PhysicsComponent.class).friction = 2);
                    rope.forEach(node -> node.add(new ChaoticMovementComponent(400, Duration.ofMillis(800))));
                    rope.root()
                        .add(new RopeOccluderComponent(ShadowOptions.angular().backdropDistance(0.45)))
                        .add(new RopeRenderComponent(Color.WHITE, 1.25))
                        .remove(PhysicsComponent.class);
                    return rope;
                })
                .assign('P', tile -> new Entity().bounds(tile.bounds().expand(-8))
                    .add(new PhysicsComponent(), p -> p.friction = 3)
                    .add(new LeftRightControlComponent())
                    .add(new BackdropOccluderComponent(ShadowOptions.angular().backdropDistance(0.75)))
                    .add(new JumpControlComponent(), j -> j.acceleration = 300)
                    .add(new CollisionSensorComponent())
                    .add(new CameraTargetComponent(1, 40))
                    .add(new CollisionDetailsComponent())
                    .add(new SuspendJumpControlComponent(), s -> s.maxJumps = 2)
                    .add(new RenderComponent(Sprite.placeholder(Color.YELLOW, 8)))))
            .addEntity(new Entity().add(new GravityComponent(Vector.y(500))))
            .addSystem(new LogFpsSystem())
            .addSystem(new InteractionSystem())
            .addEntity(new Entity().add(new TransformComponent(0, 0, 120, 40)).add(new CursorAttachmentComponent()).add(new PointLightComponent(60, Color.BLACK)).add(new GlowComponent(60, Color.WHITE.opacity(0.4))))
            .addSystem(e -> e.environment().tryFetchSingletonComponent(DirectionalLightComponent.class).ifPresent(d -> d.angle = Angle.degrees(e.mouse().position().x() / 4)))
            .addSystem(e -> e.graphics().canvas().fillWith(Color.BLUE))
            .addSystem(e -> e.graphics().camera().setZoom(e.graphics().camera().zoom() + e.mouse().unitsScrolled() / 10.0));

        engine.start();
    }

}