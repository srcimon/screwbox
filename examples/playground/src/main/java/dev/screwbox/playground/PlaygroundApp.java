package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.BackdropOccluderComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.SpotLightComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeEmitterComponent;
import dev.screwbox.core.environment.smoke.SmokeInteractionComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.WindComponent;
import dev.screwbox.core.environment.smoke.WindForceComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyBoundaryComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.light.ShadowOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.SpriteLineDrawOptions;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.graphics.smoke.styles.HeatVisionSmokeStyle;
import dev.screwbox.core.utils.TileMap;

import java.util.ArrayList;
import java.util.Random;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            ############################
            ############################
            ##                        ##
            ##                        ##
            ##            ##          ##
            ##            #           ##
            ##                        ##
            ##                        ##
            ##            T           ##
            ##                        ##
            ##                        ##
            ############################
            ############################
            """);

        screwBox.graphics().camera().setZoomRestriction(1, 10).setZoom(1);
        screwBox.graphics().configuration()
            .setIndirectLightRaySpacing(0.5)
            .setLightQuality(Percent.of(0.4))
            .setIndirectLightDiameter(6f)
            .setSmokeCellSize(2)
            .setSmokeCellPadding(64)
            .setBackgroundColor(Color.BLUE);

        Sprite sprite = Sprite.fromFile("laser.png");
        screwBox.environment().addSystem(s -> {
            Vector delta = s.mouse().position().add(200, 100);
            var target = s.navigation().raycastFrom(s.mouse().position()).castingTo(delta).nearestHit().orElse(delta);
            s.graphics().world().drawLine(s.mouse().position(), target, sprite,  new SpriteLineDrawOptions(1, 0.5, ShaderBundle.CONVEYOR.get()));
        });
        screwBox.graphics().light().setAmbientLight(Percent.of(0.5));
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(e -> {
                if(e.mouse().isDownLeft()) {
                    e.graphics().camera().move(e.mouse().drag());
                }
                e.graphics().camera().changeZoomBy(e.mouse().unitsScrolled() / -20.0);
            })
            .addEntity(new Entity().add(new CameraTargetComponent(), s -> {
                s.maxSpeed = 0;
                s.allowTeleport=true;
            }).bounds(map.bounds()))
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', source -> new Entity()
                    .bounds(source.bounds())
                    .add(new BoidObstacleComponent())
                    .add(new SmokeObstacleComponent())
                    .add(new ColliderComponent())
                    .add(new StaticBoundsComponent())
                    .add(new OccluderComponent())
                    .add(new RenderComponent(AutoTileBundle.ROCKS.get().findSprite(source.autoTileMask()))))
                .assign('T', source -> new Entity()
                    .bounds(source.bounds())
                    .add(new CursorAttachmentComponent())
                    .add(new PointLightComponent(120))
                ));

        screwBox.start();
    }
}