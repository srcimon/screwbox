package dev.screwbox.core.environment.flexphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;

@ExecutionOrder(PRESENTATION_WORLD)
public class RopeRenderSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeRenderComponent.class, RopeComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var rope : engine.environment().fetchAll(ROPES)) {
            final var config = rope.get(RopeRenderComponent.class);
            final List<Vector> nodes = new ArrayList<>();
            for (final var node : rope.get(RopeComponent.class).nodes) {
                nodes.add(node.position());
            }
            engine.graphics().world().drawPolygon(nodes, PolygonDrawOptions
                    .outline(config.color)
                    .strokeWidth(config.strokeWidth)
                    .smoothing(SPLINE));
        }
    }
}
