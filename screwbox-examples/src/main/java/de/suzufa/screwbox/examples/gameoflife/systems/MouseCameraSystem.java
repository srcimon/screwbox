package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.keyboard.Key;

public class MouseCameraSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(Key.NUMBER_1)) {
            engine.graphics().updateCameraZoom(engine.graphics().cameraZoom() + 1);
        }
        if (engine.keyboard().justPressed(Key.NUMBER_2)) {
            engine.graphics().updateCameraZoom(engine.graphics().cameraZoom() - 1);
        }
        if (engine.keyboard().isDown(Key.ARROW_DOWN)) {
            engine.graphics().moveCameraBy(Vector.yOnly(100).multiply(engine.loop().delta()));
        }
        if (engine.keyboard().isDown(Key.ARROW_RIGHT)) {
            engine.graphics().moveCameraBy(Vector.xOnly(100).multiply(engine.loop().delta()));
        }

        if (engine.keyboard().isDown(Key.ARROW_UP)) {
            engine.graphics().moveCameraBy(Vector.yOnly(-100).multiply(engine.loop().delta()));
        }
        if (engine.keyboard().isDown(Key.ARROW_LEFT)) {
            engine.graphics().moveCameraBy(Vector.xOnly(-100).multiply(engine.loop().delta()));
        }
    }

}
