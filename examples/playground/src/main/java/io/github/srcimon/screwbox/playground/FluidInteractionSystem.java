package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class FluidInteractionSystem implements EntitySystem {

    private static final Archetype INTERACTORS = Archetype.ofSpacial(FluidInteractionComponent.class);
    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        final var fluids = engine.environment().fetchAll(FLUIDS);
        final var interactors = engine.environment().fetchAll(INTERACTORS);
        for (final var fluid : fluids) {
            FluidSurface surface = fluid.get(FluidComponent.class).surface;
            for (final var interactor : interactors) {
                var irritation = interactor.get(PhysicsComponent.class).momentum.length();
                if (fluid.bounds().intersects(interactor.bounds().expandTop(surface.maxHeight()))) {//TODO necessary?
                    var point = getNearestPoint(surface.surface(fluid.origin(), fluid.bounds().width()), interactor.position());
                        surface.interact(point, irritation * engine.loop().delta() * 10);
                }
            }
        }
    }

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
