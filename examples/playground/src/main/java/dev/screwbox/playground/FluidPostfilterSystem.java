package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.fluids.FluidComponent;

import java.util.ArrayList;
import java.util.List;

public class FluidPostfilterSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class, FluidPostFilterComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.graphics().camera().changeZoomBy(engine.mouse().unitsScrolled() / 20.0);//TODO remove

        final List<Entity> fluids = engine.environment().fetchAll(FLUIDS);
        if (fluids.isEmpty()) {
            return;
        }
        final List<FluidPostFilter.Fluid> filterFluids = new ArrayList<>();
        for (final var fluid : fluids) {
            final var fluidComponent = fluid.get(FluidComponent.class);
            filterFluids.add(new FluidPostFilter.Fluid(fluidComponent.outline, fluidComponent.surface, 12));

        }
        engine.graphics().postProcessing().addEffectsFilter(new FluidPostFilter(filterFluids));
    }
}
