package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.fluids.FluidComponent;

public class FluidPostfilterSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class, FluidPostFilterComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.graphics().postProcessing().clearFilters();
        engine.graphics().camera().changeZoomBy(engine.mouse().unitsScrolled() / 20.0);

        for (final var fluid : engine.environment().fetchAll(FLUIDS)) {
            final var fluidComponent = fluid.get(FluidComponent.class);
            final var surface = fluidComponent.surface;
            final var outline = fluidComponent.outline;
            engine.graphics().postProcessing().addScreenFilter(new ExperimentalPostFilter(outline, surface));
        }
    }
}
