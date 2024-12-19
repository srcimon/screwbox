package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;

import static io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions.outline;

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
                    emitter.isEnabled ? "enabled: " + emitter.sheduler.interval().humanReadable() : "disabled",
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
