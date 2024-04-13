package io.github.srcimon.screwbox.core.particles.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.particles.ParticleComponent;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.particles.Particles;

public class DefaultParticles implements Particles, Updatable {

    private static final Archetype PARTICLES = Archetype.of(ParticleComponent.class);
    private final Engine engine;

    private int particleCount = 0;

    public DefaultParticles(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update() {
        particleCount = engine.environment().fetchAll(PARTICLES).size();
    }

    @Override
    public int particleCount() {
       return particleCount;
    }
}
