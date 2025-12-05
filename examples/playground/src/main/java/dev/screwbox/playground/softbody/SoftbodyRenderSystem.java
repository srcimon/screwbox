package dev.screwbox.playground.softbody;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;

@ExecutionOrder(PRESENTATION_WORLD)
public class SoftbodyRenderSystem implements EntitySystem {

    private static final Archetype SOFTBODIES = Archetype.ofSpacial(SoftbodyRenderComponent.class, SoftBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        final World world = engine.graphics().world();
        for (final var softbody : engine.environment().fetchAll(SOFTBODIES)) {
            final var config = softbody.get(SoftbodyRenderComponent.class);
            final List<Vector> nodes = new ArrayList<>();
            for (final var node : softbody.get(SoftBodyComponent.class).nodes) {
                nodes.add(node.position());
            }
            world.drawPolygon(nodes, PolygonDrawOptions
                    .filled(config.color)
                    .smoothing(SPLINE));

            if (!Color.TRANSPARENT.equals(config.outlineColor)) {
                world.drawPolygon(nodes, PolygonDrawOptions
                        .outline(config.outlineColor)
                        .strokeWidth(config.outlineStrokeWidth)
                        .smoothing(SPLINE));
            }
        }
    }
}
