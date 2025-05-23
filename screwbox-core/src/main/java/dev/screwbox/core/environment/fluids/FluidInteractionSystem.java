package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

/**
 * Applies waves on fluids when in contact with physics {@link Entity entities} also containing {@link FluidInteractionComponent}.
 *
 * @since 2.19.0
 */
public class FluidInteractionSystem implements EntitySystem {

    private static final Archetype INTERACTORS = Archetype.ofSpacial(FluidInteractionComponent.class);
    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        final var fluids = engine.environment().fetchAll(FLUIDS);
        final var interactors = engine.environment().fetchAll(INTERACTORS);
        for (final var entity : fluids) {
            final var fluid = entity.get(FluidComponent.class);
            for (final var interactor : interactors) {
                final var physics = interactor.get(PhysicsComponent.class);
                final var fluidInteraction = interactor.get(FluidInteractionComponent.class);
                final var irritation = Math.abs(physics.momentum.x() * fluidInteraction.xModifier) + Math.abs(physics.momentum.y() * fluidInteraction.yModifier);
                if (Math.abs(irritation) > fluidInteraction.threshold && entity.bounds().intersects(interactor.bounds().expandTop(maxHeight(fluid)))) {
                    interact(fluid, entity.bounds(), interactor.bounds(), irritation * engine.loop().delta());
                }
            }
        }
    }

    private void interact(FluidComponent fluid, final Bounds projection, final Bounds interaction, final double strength) {
        final var gap = projection.width() / (fluid.nodeCount - 1);
        for (int i = 0; i < fluid.nodeCount; i++) {
            final Vector nodePosition = projection.origin().add(i * gap, fluid.height[i]);
            if (interaction.intersects(Bounds.atPosition(nodePosition, gap, 1))) {
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
