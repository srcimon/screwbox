package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.particles.Particles;

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
