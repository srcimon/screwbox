package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.keyboard.Key;

@Order(Order.SystemOrder.OPTIMIZATION)
public class ZoomSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Camera camera = engine.graphics().primaryViewport().camera();

        if (engine.keyboard().isDown(Key.NUMBER_1)) {
            camera.changeZoomBy(engine.loop().delta(2.5));
        } else if (engine.keyboard().isDown(Key.NUMBER_2)) {
            camera.changeZoomBy(engine.loop().delta(-2.5));
        } else if (engine.keyboard().isDown(Key.NUMBER_3)) {
            camera.setZoom(3.0);
        } else if (engine.keyboard().isDown(Key.NUMBER_4)) {
            camera.setZoom(4.0);
        }
    }
}
