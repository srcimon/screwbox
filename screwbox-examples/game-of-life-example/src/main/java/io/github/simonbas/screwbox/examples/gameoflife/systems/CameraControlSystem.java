package io.github.simonbas.screwbox.examples.gameoflife.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.mouse.MouseButton;

public class CameraControlSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isDown(MouseButton.MIDDLE)) {
            engine.graphics().moveCameraBy(engine.mouse().drag());
        }

        double zoomChange = engine.mouse().unitsScrolled() * -engine.loop().delta();
        engine.graphics().updateCameraZoomBy(zoomChange);
    }

}
