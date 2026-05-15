package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.internal.LightPhysics;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

@ExecutionOrder(Order.DEBUG_OVERLAY)
public class IlluminationDebugSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        var bounds = Bounds.atPosition(engine.mouse().position(), 128, 128);
        var area = LightPhysics.DEBUG.calculateArea(bounds, 0, 360);
        engine.graphics().world().drawPolygon(area, PolygonDrawOptions.outline(Color.RED));
    }
}
