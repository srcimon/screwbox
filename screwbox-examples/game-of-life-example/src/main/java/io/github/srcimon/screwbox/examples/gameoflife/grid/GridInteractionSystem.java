package io.github.srcimon.screwbox.examples.gameoflife.grid;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entities;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.MouseButton;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridComponent;

public class GridInteractionSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(final Engine engine) {
        Entities entities = engine.entities();
        final var gridComponent = entities.forcedFetch(GRID_HOLDER).get(GridComponent.class);
        final Vector mousePosition = engine.mouse().worldPosition();

        if (engine.mouse().isDown(MouseButton.LEFT)) {
            gridComponent.grid.blockAt(mousePosition);
        }
        if (engine.mouse().isDown(MouseButton.RIGHT)) {
            gridComponent.grid.freeAt(mousePosition);
        }
    }

}
