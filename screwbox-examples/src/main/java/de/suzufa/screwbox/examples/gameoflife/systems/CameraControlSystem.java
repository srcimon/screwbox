package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.mouse.MouseButton;

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
