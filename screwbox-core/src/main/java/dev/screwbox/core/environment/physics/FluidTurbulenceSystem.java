package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

import java.util.Random;

/**
 * Adds a turbulent motion to to all fluids without need of physics {@link Entity} interaction.
 *
 * @see FluidTurbulenceComponent
 * @since 3.2.0
 */
public class FluidTurbulenceSystem implements EntitySystem {

    private static final Archetype NOISY_FLUIDS = Archetype.of(FluidComponent.class, FluidTurbulenceComponent.class);
    private static final Random RANDOM = new Random();

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(NOISY_FLUIDS)) {
            final var strength = entity.get(FluidTurbulenceComponent.class).strength;
            final var nodeSpeeds = entity.get(FluidComponent.class).speed;
            for (int i = 0; i < nodeSpeeds.length; i++) {
                nodeSpeeds[i] += RANDOM.nextDouble(-1, 1) * strength * engine.loop().delta();
            }
        }
    }
}
