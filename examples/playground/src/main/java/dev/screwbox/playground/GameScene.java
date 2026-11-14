package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.log.Log;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.map.Floor;
import dev.screwbox.playground.map.SpawnArea;
import dev.screwbox.playground.map.Wall;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;

public class GameScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(2);
        engine.loop().unlockFps();
    }

    @Override
    public void populate(Environment environment) {
        var map = TileMap.fromImageFile("map.png");


        environment
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                .assign(Color.BLACK, new Floor())
                .assign(Color.TRANSPARENT, new Wall())
                .assign(Color.RED, new SpawnArea(Color.RED))
                .assign(Color.BLUE, new SpawnArea(Color.BLUE))
                .assign(Color.WHITE, new Blueprint<TileMap.Tile<Color>>() {
                    @Override
                    public Entity assembleFrom(TileMap.Tile<Color> source) {
                        return new Entity().add(new MovementControlComponent()).add(new TransformComponent(source.bounds())).add(new PhysicsComponent()).add(new CameraTargetComponent());
                    }
                })
            );
    }
}
