package de.suzufa.screwbox.examples.platformer.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.CameraComponent;
import de.suzufa.screwbox.core.keyboard.Key;

public class ZoomSystem implements EntitySystem {

    private static final Archetype CAMERA = Archetype.of(CameraComponent.class);

    @Override
    public void update(Engine engine) {
        Entity camera = engine.entities().forcedFetch(CAMERA);

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
