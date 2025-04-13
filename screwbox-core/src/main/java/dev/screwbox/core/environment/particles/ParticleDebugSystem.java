package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;

import static dev.screwbox.core.graphics.options.RectangleDrawOptions.outline;

/**
 * Can be used to mark particles and particle emitters on the {@link Screen}.
 */
@Order(Order.SystemOrder.PRESENTATION_OVERLAY)
public class ParticleDebugSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.ofSpacial(ParticleEmitterComponent.class);
    private static final Archetype PARTICLES = Archetype.ofSpacial(RenderComponent.class, ParticleComponent.class);

    private static final LineDrawOptions PARTICLE_DRAW_OPTIONS = LineDrawOptions.color(Color.hex("#86dae6"));

    @Override
    public void update(final Engine engine) {
        final World world = engine.graphics().world();
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            world.drawRectangle(particleEmitter.bounds(), outline(emitter.isEnabled ? Color.GREEN : Color.RED)
                    .strokeWidth(2));

            world.drawText(particleEmitter.bounds().position().addY(particleEmitter.bounds().height() / 2.0 + 10),
                    emitter.isEnabled ? "enabled: " + emitter.scheduler.interval().humanReadable() : "disabled",
                    SystemTextDrawOptions.systemFont("Arial", 10).alignCenter().bold());
        }

        for (final var particle : engine.environment().fetchAll(PARTICLES)) {
            final var rotation = particle.get(RenderComponent.class).options.rotation();
            for (int i = 0; i <= 3; i++) {
                final Line line = Line.between(particle.position(), particle.position().addX(5));
                world.drawLine(rotation.add(Rotation.degrees(i * 90.0)).applyOn(line), PARTICLE_DRAW_OPTIONS);
            }
        }
    }
}
