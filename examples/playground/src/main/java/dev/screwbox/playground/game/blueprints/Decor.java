package dev.screwbox.playground.game.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiled.Tile;

public class Decor implements Blueprint<Tile> {

    @Override
    public Entity assembleFrom(Tile tile) {
        return new Entity()
            .bounds(tile.bounds())
            .add(new RenderComponent(tile.sprite(), tile.layer().order()));
    }
}
