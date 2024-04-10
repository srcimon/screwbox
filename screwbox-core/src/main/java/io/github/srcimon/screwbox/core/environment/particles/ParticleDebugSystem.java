package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions;

import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.outline;

public class ParticleDebugSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            engine.graphics().world().drawRectangle(particleEmitter.bounds(),
                    outline(emitter.isEnabled ? Color.GREEN : Color.RED)
                    .strokeWidth(2));
            engine.graphics().world().drawText(particleEmitter.bounds().position().addY(particleEmitter.bounds().height() / 2.0 + 10),
                    "spawn entities every " + emitter.sheduler.interval().humanReadable(),
                    SystemTextDrawOptions.systemFont("Arial", 10).alignCenter().bold());
        }
    }
}
