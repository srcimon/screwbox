package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

@Order(Order.SystemOrder.PRESENTATION_EFFECTS)
public class DrawWaterSystem implements EntitySystem {

    private static final Archetype WATERS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        var world = engine.graphics().world();

        for (final var water : engine.environment().fetchAll(WATERS)) {
            FluidSurface fluidSurface = water.get(FluidComponent.class).surface;
            Path surface = fluidSurface.surface(water.origin(), water.bounds().width());
            List<Vector> vectors = new ArrayList<>();
            vectors.addAll(surface.nodes());
            vectors.add(water.bounds().bottomRight());
            vectors.add(water.bounds().bottomLeft());
            world.drawPolygon(vectors, PolygonDrawOptions.filled(Color.BLUE.opacity(0.5)));
        }
    }
}
