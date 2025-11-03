package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.keyboard.Key;

@ExecutionOrder(Order.OPTIMIZATION)
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
