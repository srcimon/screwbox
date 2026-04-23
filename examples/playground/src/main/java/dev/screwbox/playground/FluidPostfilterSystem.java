package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
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
        //TODO FLuidComponent.fluidPolygon;

        for (final var fluid : engine.environment().fetchAll(FLUIDS)) {
            final var surface = fluid.get(FluidComponent.class).surface;
            final var bounds = fluid.bounds();
            final var outline = surface.addNodes(bounds.bottomRight(), bounds.bottomLeft(), bounds.origin());
            engine.graphics().postProcessing().addScreenFilter(new ExperimentalPostFilter(outline, surface));
        }
    }
}
