package io.github.srcimon.screwbox.examples.gameoflife.camera;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.MouseButton;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridComponent;

public class CameraControlSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().fetch(GRID_HOLDER).get(GridComponent.class);

        if (engine.mouse().isDown(MouseButton.MIDDLE)) {
            engine.graphics().moveCameraWithinVisualBounds(engine.mouse().drag(), gridComponent.grid.area());
        }
        Vector movement = engine.keyboard().wsadMovement(engine.loop().delta() * 80);
        engine.graphics().moveCameraWithinVisualBounds(movement, gridComponent.grid.area());


        double zoomChange = engine.mouse().unitsScrolled() * -engine.loop().delta();
        engine.graphics().updateZoomRelative(zoomChange);
    }

}
