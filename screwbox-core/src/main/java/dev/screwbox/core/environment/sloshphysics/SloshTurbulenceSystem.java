package dev.screwbox.core.environment.sloshphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

import java.util.Random;

/**
 * Adds a turbulent motion to to all slosh volumes without need of physics {@link Entity} interaction.
 *
 * @see SloshTurbulenceComponent
 * @since 3.2.0
 */
public class SloshTurbulenceSystem implements EntitySystem {

    private static final Archetype VOLUMES = Archetype.of(SloshVolumeComponent.class, SloshTurbulenceComponent.class);
    private static final Random RANDOM = new Random();

    @Override
    public void update(final Engine engine) {
        for (final var volume : engine.environment().fetchAll(VOLUMES)) {
            final var strength = volume.get(SloshTurbulenceComponent.class).strength;
            final var nodeSpeeds = volume.get(SloshVolumeComponent.class).speed;
            for (int i = 0; i < nodeSpeeds.length; i++) {
                nodeSpeeds[i] += RANDOM.nextDouble(-1, 1) * strength * engine.loop().delta();
            }
        }
    }
}
