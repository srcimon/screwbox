package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Order(Order.SystemOrder.PRESENTATION_EFFECTS)
public class FluidRenderSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class, FluidRenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(FLUIDS)) {
            FluidComponent fluid = entity.get(FluidComponent.class);
            final var outline = createOutline(fluid, entity.bounds());
            final var renderConfig = entity.get(FluidRenderComponent.class);

            final var options = isNull(renderConfig.secondaryColor)
                    ? PolygonDrawOptions.filled(renderConfig.color)
                    : PolygonDrawOptions.verticalGradient(renderConfig.color, renderConfig.secondaryColor);

            engine.graphics().world().drawPolygon(outline, options.smoothenHorizontally());
        }
    }

    private List<Vector> createOutline(final FluidComponent fluid, final Bounds bounds) {
        final var gap = bounds.width() / (fluid.nodeCount - 1);
        final List<Vector> outline = new ArrayList<>();
        for (int i = 0; i < fluid.nodeCount; i++) {
            outline.add(bounds.origin().add(i * gap, fluid.height[i]));
        }
        outline.add(bounds.bottomRight());
        outline.add(bounds.bottomLeft());
        return outline;
    }

}
