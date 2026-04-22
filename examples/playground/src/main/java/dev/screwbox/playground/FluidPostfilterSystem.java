package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.fluids.FluidComponent;

public class FluidPostfilterSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().postProcessing().clearFilters();
        engine.graphics().camera().changeZoomBy(engine.mouse().unitsScrolled() / 20.0);
        //TODO FLuidComponent.fluidPolygon;
        for (final var fluid : engine.environment().fetchAll(Archetype.ofSpacial(FluidComponent.class))) {
            var surface = fluid.get(FluidComponent.class).surface;
            var outline = surface.addNode(fluid.bounds().bottomRight()).addNode(fluid.bounds().bottomLeft()).close(); // TODO addNodes()
            engine.graphics().postProcessing().addScreenFilter(new ExperimentalPostFilter(outline, surface));
        }
    }
}
