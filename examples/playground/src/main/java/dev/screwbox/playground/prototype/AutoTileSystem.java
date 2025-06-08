package dev.screwbox.playground.prototype;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.rendering.RenderComponent;

import static dev.screwbox.core.environment.Order.SystemOrder.PRESENTATION_PREPARE;

@Order(PRESENTATION_PREPARE)
public class AutoTileSystem implements EntitySystem {

    private static final Archetype TILES = Archetype.ofSpacial(AutoTileComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {

    }
}
