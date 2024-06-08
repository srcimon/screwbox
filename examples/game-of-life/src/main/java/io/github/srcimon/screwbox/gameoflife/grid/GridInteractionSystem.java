package io.github.srcimon.screwbox.gameoflife.grid;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

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
