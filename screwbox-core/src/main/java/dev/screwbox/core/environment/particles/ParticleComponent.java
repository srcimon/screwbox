package dev.screwbox.core.environment.particles;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.particles.Particles;

import java.io.Serial;

/**
 * Marks entities that were created as particles. Used to control maximum number of particles in the {@link Environment}.
 *
 * @see Particles
 */
public class ParticleComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

}
