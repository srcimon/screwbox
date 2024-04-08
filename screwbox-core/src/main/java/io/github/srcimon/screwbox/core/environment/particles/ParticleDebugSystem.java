package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;

import java.util.List;

import static io.github.srcimon.screwbox.core.graphics.Offset.at;
import static io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions.systemFont;

public class ParticleDebugSystem implements EntitySystem {

    private static final Archetype PARTICLES = Archetype.of(ParticleComponent.class, TransformComponent.class, RenderComponent.class);
    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> particles = engine.environment().fetchAll(PARTICLES);
        final var world = engine.graphics().world();
        for (final var particle : particles) {
            var render = particle.get(RenderComponent.class);
            world.drawRectangle(Bounds.atPosition(particle.bounds().position(),
                            render.sprite.size().width() * engine.graphics().camera().zoom() * render.options.scale(),
                            render.sprite.size().height() * engine.graphics().camera().zoom() * render.options.scale()),
                    RectangleDrawOptions
                            .outline(Color.RED).
                            rotation(render.options.rotation()));
        }

        for (final var emitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            world.drawRectangle(emitter.bounds(), RectangleDrawOptions
                    .outline(Color.BLUE).strokeWidth(2));
        }

        engine.graphics().screen().drawText(at(20, 20), "Particle count: " + particles.size(), systemFont("Arial", 12).bold());
    }
}
