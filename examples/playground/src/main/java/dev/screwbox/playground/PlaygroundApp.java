package dev.screwbox.playground;

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
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeEmitterComponent;
import dev.screwbox.core.environment.smoke.SmokeInteractionComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.WindComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            ############################
            ############################
            ##                B       ##
            ##   BB                   ##
            ##   B        ##     B    ##
            ##            #           ##
            ##      B       BBB       ##
            ##      T                 ##
            ##       BBBB             ##
            ##                        ##
            ##                        ##
            ##   S         S    S     ##
            ############################
            ############################
            
            
            
            
            """);

        screwBox.graphics().camera().setZoom(3);
        screwBox.graphics().configuration()
            .setIndirectLightRaySpacing(0.2)
            .setIndirectLightDiameter(4f)
            .setSmokeCellSize(4)
            .setBackgroundColor(Color.BLUE);

        screwBox.graphics().light().setAmbientLight(Percent.of(0.5));
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addEntity(new Entity().add(new CameraTargetComponent()).bounds(map.bounds()))
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('B', source -> new Entity()
                    .bounds(source.bounds().scale(0.25))
                    .add(new OccluderComponent())
                    .add(new BoidComponent(), config -> {
                        config.velocity = 25;
                    })
                    .add(new PhysicsComponent())
                    .add(new RenderComponent(SpriteBundle.DOT_RED.get().scaled(0.25)))
                    .add(new SmokeInteractionComponent()))
                .assign('S', source -> new Entity()
                    .bounds(source.bounds())
                    .add(new SmokeEmitterComponent(25, Color.WHITE, Vector.y(-100))))
                .assign('S', source -> new Entity()
                    .bounds(source.bounds())
                    .add(new WindComponent(Vector.y(-20))))
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