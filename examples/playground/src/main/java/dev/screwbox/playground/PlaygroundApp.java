package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            #######################
            #        ###          #
            #        #            #
            #          C  #       #
            #             #      ##
            #        ###          #
            #######################
            """);

        screwBox.loop().unlockFps();
        screwBox.environment().addSystem(new LogFpsSystem());
        screwBox.graphics().configuration().setLightQuality(Percent.half());
        screwBox.graphics().camera().setZoom(3);
        screwBox.graphics().light().setAmbientLight(Percent.of(0.3));
        screwBox.environment().enableAllFeatures()
            .addEntity(new Entity().bounds(Bounds.atPosition(0, 0, 0, 0)).add(new CursorAttachmentComponent()).add(new PointLightComponent(60, Color.RED)))
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', source -> new Entity().bounds(source.bounds())
                    .add(new OccluderComponent())
                    .add(new StaticBoundsComponent())
                    .add(new RenderComponent(AutoTileBundle.ROCKS.get().findSprite(source.autoTileMask()))))
                .assign('C', source -> new Entity().bounds(source.bounds())
                    .add(new PointLightComponent(100, Color.GREEN))
                    .add(new CameraTargetComponent()))
            );
        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}