package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.camera.CameraComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class ZoomSystem implements EntitySystem {

    private static final Archetype CAMERA = Archetype.of(CameraComponent.class);

    @Override
    public void update(Engine engine) {
        Entity camera = engine.environment().fetch(CAMERA);

        if (engine.keyboard().isDown(Key.NUMBER_1)) {
            camera.get(CameraComponent.class).zoom += engine.loop().delta(2.5);
        } else if (engine.keyboard().isDown(Key.NUMBER_2)) {
            camera.get(CameraComponent.class).zoom -= engine.loop().delta(2.5);
        } else if (engine.keyboard().isDown(Key.NUMBER_3)) {
            camera.get(CameraComponent.class).zoom = 3.0;
        } else if (engine.keyboard().isDown(Key.NUMBER_4)) {
            camera.get(CameraComponent.class).zoom = 4.0;
        }

    }
}
