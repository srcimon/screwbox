package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.softphysics.RopeOccluderComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.ShadowOptions;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.core.window.MouseCursor;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            ############
            #         #
            ###   B  ##
            ###  B ########
            ##         #  #########
            ######  C #    #    #
             #       BBB #    #   #
             #######################
            """);
        screwBox.loop().unlockFps();
        screwBox.graphics().light().setAmbientLight(Percent.of(0.4));
        screwBox.graphics().camera().setZoom(3);
        screwBox.window().setCursor(MouseCursor.HIDDEN);
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new DebugSystem())
            .addSystem(new LogFpsSystem())
            .addEntity(new Entity().add(new GravityComponent(Vector.y(200))))
            .addEntity(new Entity().add(new CursorAttachmentComponent()).bounds(Bounds.$$(0, 0, 1, 1)).add(new GlowComponent(60, Color.WHITE.opacity(0.3))).add(new PointLightComponent(80, Color.BLACK)))
            .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', tile -> new Entity().name("wall")
                    .bounds(tile.bounds())
                    .add(new StaticOccluderComponent())
                    .add(new ColliderComponent())
                    .add(new StaticColliderComponent())
                    .add(new BoidObstacleComponent())
                    .add(new OccluderComponent())
                    .add(new RenderComponent(AutoTileBundle.ROCKS.get().findSprite(tile.autoTileMask())))
                )
                .assign('C', tile -> new Entity().name("camera")
                    .bounds(tile.bounds())
                    .add(new CameraTargetComponent(), c -> c.followSpeed = 10000))
                .assignComplex('R', (source, idPool) -> {
                    var rope = SoftPhysicsSupport.createRope(source.position().addY(-source.bounds().height() / 2.0), source.position().addY(10), 3, idPool);
                    rope.root().remove(PhysicsComponent.class);
                    rope.root().add(new RopeRenderComponent(Color.ORANGE, 2));
                    rope.root().add(new RopeOccluderComponent(ShadowOptions.rounded()));
                    return rope;
                })
                .assign('B', tile -> new Entity().name("boid")
                    .bounds(tile.bounds())
                    .add(new BoidComponent(), b -> {
                        b.velocity = 20;
                        b.alignmentStrenth = 8;
                        b.cohesionStrength = 6;
                        b.obstaclePerceptionRadius = 10;
                        b.obstacleAvoidanceStrength = 4;
                    })
                    .add(new PhysicsComponent())
                    .add(new RenderComponent(Sprite.pixel(Color.RED)))
                    .add(new GlowComponent(20, Color.RED.opacity(0.6)))
                    .add(new PointLightComponent(40, Color.RED))
                ));

        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}