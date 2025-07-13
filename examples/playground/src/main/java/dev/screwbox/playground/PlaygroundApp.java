package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.world.Box;
import dev.screwbox.playground.world.Gravity;
import dev.screwbox.playground.world.Player;
import dev.screwbox.playground.world.Rock;
import dev.screwbox.playground.world.Sand;
import dev.screwbox.playground.world.Water;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().configuration().setBackgroundColor(Color.hex("#02010e"));
        engine.graphics().camera().setZoom(3);

        final var map = TileMap.fromImageFile("demo.png");

        engine.environment()
                .importSource(map)
                .as(new Gravity());

        engine.environment()
                .importSource(map.blocks())
                .usingIndex(TileMap.Block::value)
                .when(Color.BLUE).as(new Water());

        engine.environment()
                .enableAllFeatures()
                .importSource(map.tiles())
                .usingIndex(TileMap.Tile::value)
                .when(Color.RED).as(new Rock())
                .when(Color.RED).randomlyAs(new Box(), Percent.of(0.02))
                .when(Color.BLACK).as(new Sand())
                .when(Color.YELLOW).as(new Player());

        engine.start();
    }
}