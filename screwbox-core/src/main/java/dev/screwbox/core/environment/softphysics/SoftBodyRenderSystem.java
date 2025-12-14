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

import java.util.List;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.NONE;
import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;
import static java.util.Objects.nonNull;

/**
 * Will render soft bodies containing a {@link SoftBodyRenderComponent}.
 *
 * @since 3.16.0
 */
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
        if (nonNull(softBody.shape)) {
            final List<Vector> nodes = softBody.shape.definitionNotes();

            if (!Color.TRANSPARENT.equals(config.color)) {
                world.drawPolygon(nodes, PolygonDrawOptions
                        .filled(config.color)
                        .smoothing(config.rounded ? SPLINE : NONE)
                        .drawOrder(config.drawOrder));
            }

            if (!Color.TRANSPARENT.equals(config.outlineColor)) {
                world.drawPolygon(nodes, PolygonDrawOptions
                        .outline(config.outlineColor)
                        .strokeWidth(config.outlineStrokeWidth)
                        .smoothing(config.rounded ? SPLINE : NONE)
                        .drawOrder(config.drawOrder));
            }
        }
    }
}
