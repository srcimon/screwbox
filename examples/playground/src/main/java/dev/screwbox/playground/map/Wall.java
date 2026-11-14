package dev.screwbox.playground.map;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.navigation.ObstacleComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.TileMap;

public class Wall implements Blueprint<TileMap.Tile<Color>> {

    private static final Asset<Sprite> SPRITE = Asset.asset(() -> Sprite.placeholder(Color.RED, Size.square(16)));

    @Override
    public Entity assembleFrom(TileMap.Tile<Color> tile) {
        return new Entity()
            .bounds(tile.bounds())
            .add(new ColliderComponent())
            .add(new ObstacleComponent())
            .add(new StaticBoundsComponent())
            .add(new RenderComponent(SPRITE));
    }
}
