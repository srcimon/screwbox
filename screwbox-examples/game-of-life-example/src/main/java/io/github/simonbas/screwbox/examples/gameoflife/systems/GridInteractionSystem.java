package io.github.simonbas.screwbox.examples.gameoflife.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.mouse.MouseButton;
import io.github.simonbas.screwbox.examples.gameoflife.components.GridComponent;

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
