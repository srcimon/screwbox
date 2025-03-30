package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

public class MouseInteractionSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.mouse().isDown(MouseButton.LEFT)) {
            for (final var fluid : engine.environment().fetchAll(FLUIDS)) {

                FluidSurface fluidSurface = fluid.get(FluidComponent.class).surface;
                Path surfacePath = fluidSurface.surface(fluid.origin(), fluid.bounds().width());

                Vector position = engine.mouse().position();

                int minNr = getNearestPoint(surfacePath, position);


                fluidSurface.interact(minNr, engine.loop().delta(4000));
            }
        }
    }

    //TODO move to path?
    //TODO duplicate
    private static int getNearestPoint(Path surfacePath, Vector position) {
        int nr = 0;
        int minNr = 0;
        double minDistance = Double.MAX_VALUE;

        for (var node : surfacePath.nodes()) {
            double distance = node.distanceTo(position);
            if (distance < minDistance) {
                minDistance = distance;
                minNr = nr;
            }
            nr++;
        }
        return minNr;
    }
}
