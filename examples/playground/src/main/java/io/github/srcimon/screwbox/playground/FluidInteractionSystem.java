package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
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
        for (final var entity : fluids) {
            FluidComponent fluid = entity.get(FluidComponent.class);
            for (final var interactor : interactors) {
                final var irritation = interactor.get(PhysicsComponent.class).momentum.length();
                final var fluidInteraction = interactor.get(FluidInteractionComponent.class);
                if (Math.abs(irritation) > fluidInteraction.threshold) {
                    if (entity.bounds().intersects(interactor.bounds().expandTop(maxHeight(fluid)))) {
                        interact(fluid, entity.bounds(), interactor.bounds(), irritation * engine.loop().delta() * fluidInteraction.modifier);
                    }
                }
            }
        }
    }

    private void interact(FluidComponent fluid, final Bounds projection, final Bounds interaction, final double strength) {
        var nodePositions = FluidSupport.calculateSurface(projection, fluid);

        for (int i = 0; i < fluid.nodeCount; i++) {
            final Vector nodePosition = nodePositions.get(i);
            if (interaction.contains(nodePosition)) {
                fluid.speed[i] += strength;
            }
        }
    }

    private double maxHeight(FluidComponent fluid) {
        double maxHeight = 0;
        for (int i = 0; i < fluid.nodeCount; i++) {
            var height = fluid.height[i];
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        return maxHeight;
    }
}
