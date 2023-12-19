package io.github.srcimon.screwbox.examples.gameoflife.grid;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

public class GridInteractionSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().forcedFetch(GRID_HOLDER).get(GridComponent.class);
        final Vector mousePosition = engine.mouse().position();

        if (engine.mouse().isDown(MouseButton.LEFT)) {
            gridComponent.grid.blockAt(mousePosition);
        }
        if (engine.mouse().isDown(MouseButton.RIGHT)) {
            gridComponent.grid.freeAt(mousePosition);
        }
    }

}
