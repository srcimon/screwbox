package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.physics.FluidComponent;
import io.github.srcimon.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            final var combinedNodes = new ArrayList<>(fluid.surface.nodes());
            Collections.addAll(combinedNodes, entity.bounds().bottomRight(), entity.bounds().bottomLeft());
            final var outline = (List<Vector>) combinedNodes;
            final var renderConfig = entity.get(FluidRenderComponent.class);

            final var options = isNull(renderConfig.secondaryColor)
                    ? PolygonDrawOptions.filled(renderConfig.color)
                    : PolygonDrawOptions.verticalGradient(renderConfig.color, renderConfig.secondaryColor);

            engine.graphics().world().drawPolygon(outline, options.smoothenHorizontally());
        }
    }

}
