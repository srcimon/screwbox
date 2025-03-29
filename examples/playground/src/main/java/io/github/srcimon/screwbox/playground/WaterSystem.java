package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;

public class WaterSystem implements EntitySystem {

    private WaterSurface waterSurface = new WaterSurface(Line.between(Vector.$(-140, 0), Vector.$(140, 0)), 8);

    @Override
    public void update(Engine engine) {
        for (final var node : waterSurface.nodes()) {
            engine.graphics().world().drawCircle(node.position(), 4, CircleDrawOptions.filled(Color.WHITE));
        }
    }
}
