package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

public class DrawWaterSystem implements EntitySystem {

    private static final Archetype WATERS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        var nodeOptions = CircleDrawOptions.filled(Color.hex("#7878e7"));
        var nodeOutlineOptions = CircleDrawOptions.outline(Color.WHITE).strokeWidth(2);
        var segmentOptions = LineDrawOptions.color(Color.WHITE).strokeWidth(2);
        var world = engine.graphics().world();

        for (final var water : engine.environment().fetchAll(WATERS)) {
            WaterSurface waterSurface = water.get(FluidComponent.class).waterSurface;
            Path surfacePath = waterSurface.surface(water.origin(), water.bounds().width());
            List<Vector> vectors = new ArrayList<>();
            vectors.addAll(surfacePath.nodes());
            vectors.add(water.bounds().bottomRight());
            vectors.add(water.bounds().bottomLeft());
            world.drawPolygon(vectors, PolygonDrawOptions.filled(Color.BLUE.opacity(0.5)));
            for (final var segment : surfacePath.segments()) {
                world.drawLine(segment, segmentOptions);
            }

            for (final var node : surfacePath.nodes()) {
                world.drawCircle(node, 3, nodeOptions);
                world.drawCircle(node, 6, nodeOutlineOptions);
            }
        }
    }
}
