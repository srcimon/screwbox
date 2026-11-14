package dev.screwbox.playground.gamescene;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.playground.gamescene.mapimport.Floor;
import dev.screwbox.playground.gamescene.mapimport.SpawnArea;
import dev.screwbox.playground.gamescene.mapimport.Wall;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Map;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;
import static dev.screwbox.core.environment.importing.ImportOptions.source;

public class GameScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(3);
    }

    @Override
    public void populate(Environment environment) {
        var map = Map.fromJson("map.json");

        environment
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground.gamescene")
            .importSource(source(map)
                .make(new MapConfig()))

            .importSource(indexedSources(map.tiles(), tile -> tile.layer().name())
                .assign("floor", new Floor())
                .assign("wall", new Wall())
            )

            .importSource(indexedSources(map.objects(), GameObject::name)
                .assign("respawn-area", new SpawnArea())
            );
    }
}
