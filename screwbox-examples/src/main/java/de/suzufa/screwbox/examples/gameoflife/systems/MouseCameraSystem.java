package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.core.mouse.MouseButton;

public class MouseCameraSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isDown(MouseButton.LEFT)) {
            engine.graphics().moveCameraBy(engine.mouse().drag());
        }
        if (engine.keyboard().justPressed(Key.NUMBER_1)) {
            engine.graphics().updateCameraZoom(engine.graphics().cameraZoom() + 0.5);
        }
        if (engine.keyboard().justPressed(Key.NUMBER_2)) {
            engine.graphics().updateCameraZoom(engine.graphics().cameraZoom() - 0.5);
        }
    }

}
