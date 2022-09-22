package de.suzufa.screwbox.examples.gameoflife.systems;

import static de.suzufa.screwbox.core.Bounds.atPosition;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.mouse.MouseButton;
import de.suzufa.screwbox.examples.gameoflife.components.GridComponent;

public class GridInteractionSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isDown(MouseButton.RIGHT)) {
            var gridComponent = engine.entities().forcedFetch(GRID_HOLDER).get(GridComponent.class);
            gridComponent.grid.blockArea(atPosition(engine.mouse().worldPosition(), 1, 1));
        }
    }

}
