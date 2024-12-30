package io.github.srcimon.screwbox.physicsplayground.scenes;

import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.physicsplayground.camera.CameraBounds;
import io.github.srcimon.screwbox.physicsplayground.player.Player;
import io.github.srcimon.screwbox.physicsplayground.player.PlayerControlSystem;
import io.github.srcimon.screwbox.physicsplayground.player.ResetSceneSystem;
import io.github.srcimon.screwbox.physicsplayground.tiles.DecorTile;
import io.github.srcimon.screwbox.physicsplayground.tiles.HazardTile;
import io.github.srcimon.screwbox.physicsplayground.tiles.SolidTile;
import io.github.srcimon.screwbox.physicsplayground.water.Water;
import io.github.srcimon.screwbox.tiled.Map;

public class PlaygroundScene implements Scene {

    @Override
    public void populate(Environment environment) {
        var map = Map.fromJson("playground-map.json");

        environment.enableAllFeatures()
                .addSystem(new ResetSceneSystem())
                .addSystem(new PlayerControlSystem());

        environment.importSource(map)
                .as(new CameraBounds());

        environment.importSource(map.tiles())
                .usingIndex(tile -> tile.layer().name())
                .when("solid").as(new SolidTile())
                .when("hazard").as(new HazardTile())
                .when("decor").as(new DecorTile());

        environment.importSource(map.objects())
                .usingIndex(object -> object.name())
                .when("player").as(new Player())
                .when("water").as(new Water());
    }
}
