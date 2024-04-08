package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.io.Serial;

//TODO environment().enableParticles()
public class ParticleEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean isActive = true;
    public boolean useAreaSpawn = true;
    public Sheduler sheduler;
    public ParticleFactory particleFactory;

    public ParticleEmitterComponent(final Sheduler sheduler, final ParticleFactory particleFactory) {
        this.sheduler = sheduler;
        this.particleFactory = particleFactory;
    }
}
