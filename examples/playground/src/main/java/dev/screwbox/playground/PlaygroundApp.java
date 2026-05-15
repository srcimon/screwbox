package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            ###########
            #         #
            ###      ##
            ###    #######
            ##           #
            ######  C    #
             #           #
             #############
            """);

        screwBox.graphics().light().setAmbientLight(Percent.of(0.9));
        screwBox.graphics().camera().setZoom(3);
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new IlluminationDebugSystem())
            .addSystem(new LogFpsSystem())
            .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', tile -> new Entity().name("wall")
                    .bounds(tile.bounds())
                    .add(new StaticOccluderComponent())
                    .add(new OccluderComponent())
                    .add(new RenderComponent(AutoTileBundle.ROCKS.get().findSprite(tile.autoTileMask())))
                )
                .assign('C', tile -> new Entity().name("camera")
                    .bounds(tile.bounds())
                    .add(new CameraTargetComponent(), c -> c.followSpeed = 10000)
                ));

        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}