package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;

import static io.github.srcimon.screwbox.core.Vector.$;

public class DrawWaterSystem implements EntitySystem {

    private WaterSurface waterSurface = new WaterSurface(Line.between($(-240, 0), $(240, 0)), 8);

    @Override
    public void update(Engine engine) {
        var nodeOptions = CircleDrawOptions.filled(Color.hex("#7878e7"));
        var nodeOutlineOptions = CircleDrawOptions.outline(Color.WHITE).strokeWidth(2);
        var segmentOptions = LineDrawOptions.color(Color.WHITE).strokeWidth(2);

        World world = engine.graphics().world();

        for (final var segment : waterSurface.surface().segments()) {
            world.drawLine(segment, segmentOptions);
        }

        for (final var node : waterSurface.nodes()) {
            world.drawCircle(node.position(), 8, nodeOptions);
            world.drawCircle(node.position(), 8, nodeOutlineOptions);
        }
    }
}
