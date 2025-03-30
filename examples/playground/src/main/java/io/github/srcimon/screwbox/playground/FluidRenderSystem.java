package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.options.PolygonDrawOptions;

@Order(Order.SystemOrder.PRESENTATION_EFFECTS)
public class FluidRenderSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class, FluidRenderComponent.class);

    @Override
    public void update(Engine engine) {

        for (final var fluid : engine.environment().fetchAll(FLUIDS)) {
            final Color color = fluid.get(FluidRenderComponent.class).color;
            final var outline = fluid.get(FluidComponent.class).surface.outline(fluid.bounds());
            engine.graphics().world().drawPolygon(outline, PolygonDrawOptions.filled(color));
        }
    }
}
