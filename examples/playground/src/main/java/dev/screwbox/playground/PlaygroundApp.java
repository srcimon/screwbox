package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
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
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeEmitterComponent;
import dev.screwbox.core.environment.smoke.SmokeInteractionComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.graphics.smoke.styles.NettrunnerSmokeStyle;
import dev.screwbox.core.utils.Noise;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            #######################
            #        ###          #
            #        #            #
            #        #            #
            #                     #
            #           BB        #
            #                     #
            #     D    C          #
            #   ####              #
            #        B            #
            #  B        BB        #
            #             #       #
            #             #      ##
            #  D      ###     D   #
            #######################
            """);

        screwBox.graphics().smoke().setOptions(SmokeOptions.slowFade().diffusion(Percent.of(0.00000001)).opacity(Percent.half()).style(new NettrunnerSmokeStyle()));
        screwBox.graphics().configuration().setSmokeCellSize(4);

        screwBox.environment().addSystem(new LogFpsSystem());
        screwBox.graphics().configuration().setLightQuality(Percent.half());
        screwBox.graphics().camera().setZoom(3);
        screwBox.graphics().light().setAmbientLight(Percent.of(0.3));
        screwBox.environment().enableAllFeatures()
            .addEntity(new Entity().bounds(Bounds.atPosition(0, 0, 8, 8)).add(new CursorAttachmentComponent())
                .add(new PointLightComponent(120, Color.BLACK))
                .add(new GlowComponent(80, Color.WHITE.opacity(0.4))))
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('B', source -> new Entity().bounds(source.bounds().scale(0.25))
                    .add(new PhysicsComponent())
                        .add(new BoidComponent(), v -> {
                            v.velocity=50;
                        })
                        .add(new RenderComponent(Sprite.placeholder(Color.WHITE, Size.square(4))))
                        .add(new GlowComponent(20, Color.YELLOW.opacity(0.2)))
                        .add(new PointLightComponent(30, Color.BLACK))
                    )
                .assign('#', source -> new Entity().bounds(source.bounds())
                    .add(new OccluderComponent())
                    .add(new BoidObstacleComponent())
                    .add(new SmokeObstacleComponent())
                    .add(new StaticBoundsComponent())
                    .add(new RenderComponent(AutoTileBundle.ROCKS.get().findSprite(source.autoTileMask()))))
                .assign('C', source -> new Entity().bounds(source.bounds())
                    .add(new PointLightComponent(100, Color.BLACK))
                    .add(new CameraTargetComponent()))
                .assign('D', source -> new Entity().bounds(source.bounds())
                    .add(new SmokeEmitterComponent(4, Color.WHITE, Vector.y(-500)), config -> {
                        config.velocityTurbulenceRange = Angle.degrees(60);
                        config.velocityTurbulence = Noise.fixedInterval(Duration.ofMillis(200));

                    }))
                   );
        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}