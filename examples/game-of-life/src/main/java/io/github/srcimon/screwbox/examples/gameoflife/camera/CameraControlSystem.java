package io.github.srcimon.screwbox.examples.gameoflife.camera;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.MouseButton;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridComponent;

public class CameraControlSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var gridArea = engine.environment().fetchSingletonComponent(GridComponent.class).grid.area();

        if (engine.mouse().isDown(MouseButton.MIDDLE)) {
            engine.graphics().moveCameraWithinVisualBounds(engine.mouse().drag(), gridArea);
        }
        Vector movement = engine.keyboard().wsadMovement(engine.loop().delta() * 80);
        engine.graphics().moveCameraWithinVisualBounds(movement, gridArea);

        double zoomChange = engine.mouse().unitsScrolled() * -engine.loop().delta();
        engine.graphics().updateZoomRelative(zoomChange);
    }

}
