package dev.screwbox.playground.softbody;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.playground.rope.RopeComponent;
import dev.screwbox.playground.rope.RopeRenderComponent;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;

@ExecutionOrder(PRESENTATION_WORLD)
public class SoftbodyRenderSystem implements EntitySystem {

    private static final Archetype SOFTBODIES = Archetype.ofSpacial(SoftbodyRenderComponent.class, SoftbodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var softbody : engine.environment().fetchAll(SOFTBODIES)) {
            final var config = softbody.get(SoftbodyRenderComponent.class);
            final List<Vector> nodes = new ArrayList<>();
            for (final var node : softbody.get(SoftbodyComponent.class).nodes) {
                nodes.add(node.position());
            }
            engine.graphics().world().drawPolygon(nodes, PolygonDrawOptions
                    .outline(config.color)
                    .smoothing(SPLINE));
        }
    }
}
