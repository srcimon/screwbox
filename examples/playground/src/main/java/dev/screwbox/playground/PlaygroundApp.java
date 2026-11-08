package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraLeadComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.graphics.smoke.styles.NettrunnerSmokeStyle;
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
            #                     #
            #                     #
            #                     #
            #   ####   P          #
            #                     #
            #                     #
            #             #       #
            #             #      ##
            #         ###         #
            #######################
            """);

        screwBox.graphics().smoke().setOptions(SmokeOptions.slowFade().diffusion(Percent.of(0.00000001)).opacity(Percent.half()).style(new NettrunnerSmokeStyle()));
        screwBox.graphics().configuration().setSmokeCellSize(4);
        screwBox.environment().addSystem(new LogFpsSystem());
        screwBox.graphics().configuration().setLightQuality(Percent.half());
        screwBox.graphics().camera().setZoom(4);
        screwBox.environment().enableAllFeatures()
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', source -> new Entity().bounds(source.bounds())
                    .add(new ColliderComponent())
                    .add(new StaticBoundsComponent())
                    .add(new RenderComponent(AutoTileBundle.BUBBLEGUM.get().findSprite(source.autoTileMask()))))
                .assign('P', source -> new Entity().bounds(source.bounds().scale(0.75))
                    .add(new PhysicsComponent(), config -> config.friction = 4)
                    .add(new CameraTargetComponent(60))
                    .add(new CameraLeadComponent(), config -> {
                        config.adjustmentSpeed = 0.8;
                        config.xModifier = 0.6;
                        config.yModifier = 0.6;
                    })
                    .add(new MovementControlComponent(), config -> {
                        config.acceleration = 400;
                        config.maxSpeed = 100;
                    })
                    .add(new RenderComponent(Sprite.placeholder(Color.RED, Size.square(12)))))
            );
        screwBox.graphics().configuration().setBackgroundColor(Color.WHITE);
        screwBox.start();
    }

}