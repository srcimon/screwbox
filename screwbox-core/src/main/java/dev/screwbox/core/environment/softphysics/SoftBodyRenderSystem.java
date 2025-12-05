package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;

@ExecutionOrder(PRESENTATION_WORLD)
public class SoftBodyRenderSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyRenderComponent.class, SoftBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        final World world = engine.graphics().world();
        for (final var body : engine.environment().fetchAll(BODIES)) {
            renderSoftBody(world, body);
        }
    }

    private static void renderSoftBody(final World world, final Entity body) {
        final var config = body.get(SoftBodyRenderComponent.class);
        final var softBody = body.get(SoftBodyComponent.class);
        if (!softBody.nodes.isEmpty()) {
            final List<Vector> nodes = new ArrayList<>();
            for (final var node : softBody.nodes) {
                nodes.add(node.position());
            }

            nodes.add(nodes.getFirst());

            world.drawPolygon(nodes, PolygonDrawOptions
                    .filled(config.color)
                    .smoothing(SPLINE)
                    .drawOrder(config.drawOrder));

            if (!Color.TRANSPARENT.equals(config.outlineColor)) {
                world.drawPolygon(nodes, PolygonDrawOptions
                        .outline(config.outlineColor)
                        .strokeWidth(config.outlineStrokeWidth)
                        .smoothing(SPLINE)
                        .drawOrder(config.drawOrder));
            }
        }
    }
}
