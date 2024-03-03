package io.github.srcimon.screwbox.examples.gameoflife.camera;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.mouse.MouseButton;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridComponent;

public class CameraControlSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var gridArea = engine.environment().fetchSingletonComponent(GridComponent.class).grid.area();
        Camera camera = engine.graphics().camera();

        if (engine.mouse().isDown(MouseButton.MIDDLE)) {
            camera.moveWithinVisualBounds(engine.mouse().drag(), gridArea);
        }
        Vector movement = engine.keyboard().wsadMovement(engine.loop().delta() * 80);
        camera.moveWithinVisualBounds(movement, gridArea);

        double zoomChange = engine.mouse().unitsScrolled() * -engine.loop().delta();
        camera.changeZoomBy(zoomChange);
    }

}
