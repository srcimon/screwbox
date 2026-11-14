package dev.screwbox.playground;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.TileMap;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;

public class GameScene implements Scene {

    @Override
    public void populate(Environment environment) {
        var map = TileMap.fromImageFile("map.png");

        environment
            .enableAllFeatures()
            .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                .assign(Color.BLACK, new Blueprint<TileMap.Tile<Color>>() {
                    @Override
                    public Entity assembleFrom(TileMap.Tile<Color> tile) {
                        return new Entity()
                            .bounds(tile.bounds())
                            .add(new StaticBoundsComponent())
                            .add(new RenderComponent(Sprite.placeholder(Color.GREY, Size.square(16))));
                    }
                })
                .assign(Color.TRANSPARENT, new Blueprint<TileMap.Tile<Color>>() {
                    @Override
                    public Entity assembleFrom(TileMap.Tile<Color> tile) {
                        return new Entity()
                            .bounds(tile.bounds())
                            .add(new ColliderComponent())
                            .add(new StaticBoundsComponent())
                            .add(new RenderComponent(Sprite.placeholder(Color.RED, Size.square(16))));
                    }
                })
            );
    }
}
