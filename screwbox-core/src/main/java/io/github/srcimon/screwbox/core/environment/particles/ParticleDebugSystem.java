package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions;

import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.outline;

public class ParticleDebugSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(TransformComponent.class, ParticleEmitterComponent.class);
    private static final Archetype PARTICLES = Archetype.of(TransformComponent.class, RenderComponent.class, ParticleComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            engine.graphics().world().drawRectangle(particleEmitter.bounds(),
                    outline(emitter.isEnabled ? Color.GREEN : Color.RED)
                            .strokeWidth(2));
            engine.graphics().world().drawText(particleEmitter.bounds().position().addY(particleEmitter.bounds().height() / 2.0 + 10),
                    emitter.isEnabled ? "enabled: " + emitter.sheduler.interval().humanReadable() : "disabled",
                    SystemTextDrawOptions.systemFont("Arial", 10).alignCenter().bold());
        }

        for (final var particle : engine.environment().fetchAll(PARTICLES)) {
            engine.graphics().world().drawRectangle(particle.bounds(),
                    outline(Color.GREY).rotation(particle.get(RenderComponent.class).options.rotation()));
        }
    }
}
