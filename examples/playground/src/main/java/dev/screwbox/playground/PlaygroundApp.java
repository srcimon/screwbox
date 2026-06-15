package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {

    public static void main(String[] args) {

        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().camera().setZoom(3);

        var map = TileMap.fromString("""
            #####
            #          #
            #          #
            #          #
            #    T     #
            #          #
            #          #
            ####   #####
            """);

        screwBox.environment().enableAllFeatures()
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', source -> new Entity().bounds(source.bounds()).add(new RenderComponent(Sprite.placeholder(Color.RED, source.size()))))
                .assign('T', source -> new Entity().bounds(source.bounds())
                    .add(new CameraTargetComponent())
                    .add(new RenderComponent(FontBundle.BOLDZILLA.get().spriteFor('T').get())))
            );

        screwBox.start();
    }

}