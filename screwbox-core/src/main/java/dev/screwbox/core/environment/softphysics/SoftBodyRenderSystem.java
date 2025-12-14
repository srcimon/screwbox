package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

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
            renderSoftBody(body, world);
        }
    }

    private static void renderSoftBody(final Entity body, final World world) {
        final var config = body.get(SoftBodyRenderComponent.class);
        final var softBody = body.get(SoftBodyComponent.class);
        if (nonNull(softBody.shape)) {
            if (config.color.isVisible()) {
                world.drawPolygon(softBody.shape.definitionNotes(), PolygonDrawOptions
                        .filled(config.color)
                        .smoothing(config.rounded ? SPLINE : NONE)
                        .drawOrder(config.drawOrder));
            }

            if (config.outlineColor.isVisible()) {
                world.drawPolygon(softBody.shape.definitionNotes(), PolygonDrawOptions
                        .outline(config.outlineColor)
                        .strokeWidth(config.outlineStrokeWidth)
                        .smoothing(config.rounded ? SPLINE : NONE)
                        .drawOrder(config.drawOrder));
            }
        }
    }
}
