package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;

public class ParticleSystem implements EntitySystem {

    private static final Archetype EMITTERS = Archetype.of(ParticleEmitterComponent.class, TransformComponent.class);

        Sheduler sheduer = Sheduler.withInterval(Duration.ofMillis(200));

    @Override
    public void update(Engine engine) {
        for(final var emitter : engine.entities().fetchAll(EMITTERS)) {
        }
    }
}
