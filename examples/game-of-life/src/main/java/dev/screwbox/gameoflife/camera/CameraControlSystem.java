package dev.screwbox.gameoflife.camera;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.mouse.MouseButton;
import dev.screwbox.gameoflife.grid.GridComponent;

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
