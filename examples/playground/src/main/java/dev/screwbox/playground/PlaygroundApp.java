package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.utils.AsciiMap;
import dev.screwbox.playground.world.Gravity;
import dev.screwbox.playground.world.Player;
import dev.screwbox.playground.world.Wall;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(3);

        final var map = AsciiMap.fromString("""
                
                
                
                          P
                ##### ########
                #####   ###  ####
                ## #    # #
                """);

        engine.environment()
                .importSource(map)
                .as(new Gravity());

        engine.environment()
                .enableAllFeatures()
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('#').as(new Wall())
                .when('P').as(new Player());

        engine.start();
    }
}