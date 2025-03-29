package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;

public class DrawWaterSystem implements EntitySystem {

    private WaterSurface waterSurface = new WaterSurface(Line.between(Vector.$(-240, 0), Vector.$(240, 0)), 8);

    @Override
    public void update(Engine engine) {
        var nodeOptions = CircleDrawOptions.filled(Color.WHITE);
        for (final var segment : waterSurface.surface().segments()) {
            engine.graphics().world().drawLine(segment, LineDrawOptions.color(Color.WHITE).strokeWidth(2));
        }
        for (final var node : waterSurface.nodes()) {
            engine.graphics().world().drawCircle(node.position(), 8, nodeOptions);
        }
    }
}
