package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.Collections;

import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.HORIZONTAL;
import static java.util.Objects.isNull;

/**
 * Will render all fluids also containing {@link FluidRenderComponent}.
 *
 * @since 2.19.0
 */
@Order(Order.SystemOrder.PRESENTATION_EFFECTS)
public class FluidRenderSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class, FluidRenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(FLUIDS)) {
            final var fluid = entity.get(FluidComponent.class);
            final var outline = new ArrayList<>(fluid.surface.nodes());
            Collections.addAll(outline, entity.bounds().bottomRight(), entity.bounds().bottomLeft());
            final var renderConfig = entity.get(FluidRenderComponent.class);

            final var options = isNull(renderConfig.secondaryColor)
                    ? PolygonDrawOptions.filled(renderConfig.color)
                    : PolygonDrawOptions.verticalGradient(renderConfig.color, renderConfig.secondaryColor);

            engine.graphics().world().drawPolygon(outline, options.smoothing(HORIZONTAL));
        }
    }

}
