package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.mouse.MouseButton;

public class GridInteractionSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var grid = engine.environment().fetchSingletonComponent(GridComponent.class).grid;
        final Vector mousePosition = engine.mouse().position();

        if (engine.mouse().isDown(MouseButton.LEFT)) {
            grid.blockAt(mousePosition);
        }
        if (engine.mouse().isDown(MouseButton.RIGHT)) {
            grid.freeAt(mousePosition);
        }
    }

}
