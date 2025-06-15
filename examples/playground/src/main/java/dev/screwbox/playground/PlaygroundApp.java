package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.AsciiMap;
import dev.screwbox.playground.world.Gravity;
import dev.screwbox.playground.world.Player;
import dev.screwbox.playground.world.Rock;
import dev.screwbox.playground.world.Water;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().configuration().setBackgroundColor(Color.hex("#02010e"));
        engine.graphics().camera().setZoom(3);

        final var map = AsciiMap.fromString("""
                
                   ###   #
                         #
                   ####  #  #
                   ####
                   ###
                   ###
                
                  #####              ######
                  #######  P        ##########
                ######################  ######
                ############### ##########  ###
                WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
                WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
                WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
                WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
                """);

        engine.environment()
                .importSource(map)
                .as(new Gravity());

        engine.environment()
                .importSource(map.blocks())
                .usingIndex(AsciiMap.Block::value)
                .when('W').as(new Water());

        engine.environment()
                .enableAllFeatures()
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('#').as(new Rock())
                .when('P').as(new Player());

        engine.start();
    }
}