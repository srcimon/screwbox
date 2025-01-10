package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playground.movement.AccelerationSystem;
import io.github.srcimon.screwbox.playground.movement.MovementControlSystem;
import io.github.srcimon.screwbox.playground.player.Player;
import io.github.srcimon.screwbox.playground.world.Gravity;
import io.github.srcimon.screwbox.playground.world.Ground;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().camera().setZoom(2);
        AsciiMap map = AsciiMap.fromString("""
                         p
                ####################
                """);
        screwBox.environment()
                .enableAllFeatures()
                .addEntity()
                .addSystem(new MovementControlSystem())
                .addSystem(new AccelerationSystem())
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('#').as(new Ground())
                .when('p').as(new Player());

        screwBox.environment()
                .importSource(map)
                .as(new Gravity());

        screwBox.start();
    }
}