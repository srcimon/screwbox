package dev.screwbox.core.environment.sloshphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

/**
 * Applies waves on fluids when in contact with physics {@link Entity entities} also containing {@link SloshInteractionComponent}.
 *
 * @since 2.19.0
 */
public class SloshInteractionSystem implements EntitySystem {

    private static final Archetype INTERACTORS = Archetype.ofSpacial(SloshInteractionComponent.class, PhysicsComponent.class);
    private static final Archetype SLOSH = Archetype.ofSpacial(SloshVolumeComponent.class);

    @Override
    public void update(final Engine engine) {
        final var sloshs = engine.environment().fetchAll(SLOSH);
        final var interactors = engine.environment().fetchAll(INTERACTORS);
        for (final var entity : sloshs) {
            final var fluid = entity.get(SloshVolumeComponent.class);
            for (final var interactor : interactors) {
                final var physics = interactor.get(PhysicsComponent.class);
                final var fluidInteraction = interactor.get(SloshInteractionComponent.class);
                final var irritation = Math.abs(physics.velocity.x() * fluidInteraction.xModifier) + Math.abs(physics.velocity.y() * fluidInteraction.yModifier);
                if (Math.abs(irritation) > fluidInteraction.threshold && entity.bounds().intersects(interactor.bounds().expandTop(maxHeight(fluid)))) {
                    interact(fluid, entity.bounds(), interactor.bounds(), irritation * engine.loop().delta());
                }
            }
        }
    }

    private static void interact(final SloshVolumeComponent fluid, final Bounds projection, final Bounds interaction, final double strength) {
        final var gap = projection.width() / (fluid.nodeCount - 1);
        for (int i = 0; i < fluid.nodeCount; i++) {
            final Vector nodePosition = projection.origin().add(i * gap, fluid.height[i]);
            if (interaction.intersects(Bounds.atPosition(nodePosition, gap, 1))) {
                fluid.speed[i] += strength;
            }
        }
    }

    private static double maxHeight(final SloshVolumeComponent fluid) {
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
