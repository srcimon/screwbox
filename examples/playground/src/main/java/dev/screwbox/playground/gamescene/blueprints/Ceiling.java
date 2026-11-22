package dev.screwbox.playground.gamescene.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiled.Tile;

public class Ceiling  implements Blueprint<Tile> {

    @Override
    public Entity assembleFrom(Tile tile) {
        return new Entity()
            .bounds(tile.bounds())
            .add(new RenderComponent(tile.sprite(), Order.PRESENTATION_LIGHT.mixinDrawOrder(1)));
    }
}
