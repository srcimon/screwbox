package io.github.srcimon.screwbox.examples.gameoflife.camera;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

public class CameraControlSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isDown(MouseButton.MIDDLE)) {
            engine.graphics().moveCamera(engine.mouse().drag());
        }

        double zoomChange = engine.mouse().unitsScrolled() * -engine.loop().delta();
        engine.graphics().updateZoomRelative(zoomChange);
    }

}
