package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

public class FluidInteractionSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isDown(MouseButton.LEFT)) {
            for (final var fluid : engine.environment().fetchAll(FLUIDS)) {

                FluidSurface fluidSurface = fluid.get(FluidComponent.class).surface;
                Path surfacePath = fluidSurface.surface(fluid.origin(), fluid.bounds().width());


                int nr = 0;
                int minNr = 0;
                double minDistance = Double.MAX_VALUE;

                for (var node : surfacePath.nodes()) {
                    double distance = node.distanceTo(engine.mouse().position());
                    if (distance < minDistance) {
                        minDistance = distance;
                        minNr = nr;
                    }
                    nr++;
                }


                fluidSurface.interact(minNr, engine.loop().delta(4000));
            }
        }
    }
}
