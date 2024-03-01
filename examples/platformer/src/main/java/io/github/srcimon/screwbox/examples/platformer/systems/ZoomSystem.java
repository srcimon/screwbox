package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.camera.CameraComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;

@Order(SystemOrder.OPTIMIZATION)
public class ZoomSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {

        if (engine.keyboard().isDown(Key.NUMBER_1)) {
            engine.graphics().updateZoomRelative(engine.loop().delta(2.5));
        } else if (engine.keyboard().isDown(Key.NUMBER_2)) {
            engine.graphics().updateZoomRelative(engine.loop().delta(-2.5));
        } else if (engine.keyboard().isDown(Key.NUMBER_3)) {
            engine.graphics().updateZoom(3.0);
        } else if (engine.keyboard().isDown(Key.NUMBER_4)) {
            engine.graphics().updateZoom(4.0);
        }

    }
}
