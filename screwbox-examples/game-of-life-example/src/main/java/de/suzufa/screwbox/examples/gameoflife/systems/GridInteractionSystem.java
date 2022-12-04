package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.mouse.MouseButton;
import de.suzufa.screwbox.examples.gameoflife.components.GridComponent;

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
