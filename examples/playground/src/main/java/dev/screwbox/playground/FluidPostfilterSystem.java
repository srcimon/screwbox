package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

public class FluidPostfilterSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().postProcessing().clearFilters();
        //TODO FLuidComponent.fluidPolygon;
        for(final var fluid : engine.environment().fetchAll(Archetype.ofSpacial(FluidComponent.class))) {
            var surface = fluid.get(FluidComponent.class).surface;
            var outline = surface.addNode(fluid.bounds().bottomRight()).addNode(fluid.bounds().bottomLeft()).close(); // TODO addNodes()
            engine.graphics().postProcessing().addScreenFilter(new ExperimentalPostFilter(outline, surface));
        }
    }
}
