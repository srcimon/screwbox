package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.light.ConeLightComponent;
import dev.screwbox.core.environment.light.DirectionalLightComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;
import dev.screwbox.core.environment.physics.TailwindPropelledComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.softphysics.RopeOccluderComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.ShadowOptions;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.core.window.MouseCursor;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            ############
            #     R    #
            ###        ##
            ###     #######
            ##        ##  ########
            ######    #C    #    #
             #           #    #  #
             #####################
            """);
        screwBox.loop().unlockFps();
        screwBox.graphics().light().setAmbientLight(Percent.of(0.4));
        screwBox.graphics().camera().setZoom(3);
        screwBox.window().setCursor(MouseCursor.HIDDEN);
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new DebugSystem())
            .addSystem(new LogFpsSystem())
            .addEntity(new Entity().bounds(map.bounds().scale(3)).add(new DirectionalLightComponent(), d -> d.angle = Angle.degrees(-10)))
            .addEntity(new Entity().add(new GravityComponent(Vector.y(200))))
            .addEntity(new Entity().add(new CursorAttachmentComponent()).bounds(Bounds.$$(0, 0, 1, 1)).add(new TailwindComponent(40)).add(new GlowComponent(60, Color.WHITE.opacity(0.3))).add(new PointLightComponent(80, Color.BLACK)))
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
                    var rope = SoftPhysicsSupport.createRope(source.position().addY(-source.bounds().height() / 2.0), source.position().addY(10), 6, idPool);
                    rope.end().remove(PhysicsComponent.class);
                    rope.end().moveTo(rope.root().position());
                    rope.root().add(new RopeRenderComponent(Color.BLACK, 1));
                    rope.end().add(new RopeOccluderComponent(ShadowOptions.rounded()));
                    rope.connectors().forEach(r -> r.get(PhysicsComponent.class).friction = 1);
                    rope.connectors().forEach(r -> r.add(new TailwindPropelledComponent()));
                    Entity lamp = new Entity().name("lamp")
                        .add(new AttachmentComponent(rope.root().forceId()))
                        .bounds(Bounds.atPosition(rope.end().position(), 1, 1))
                        .add(new GlowComponent(30, Color.WHITE.opacity(0.5))).add(new PointLightComponent(20)).add(new ConeLightComponent(Angle.degrees(0), Angle.degrees(120), 100, Color.BLACK));
                    return ListUtil.combine(rope, lamp);
                }));

        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}