package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Viewport;

/**
 * Updates the {@link CameraTargetComponent#offset} for all {@link Entity entities} having a {@link CameraLeadComponent}.
 *
 * @since 3.32.0
 */
public class CameraLeadSystem implements EntitySystem {

    private static final Archetype CAMERAS = Archetype.of(CameraLeadComponent.class, CameraTargetComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var target : engine.environment().fetchAll(CAMERAS)) {
            final var camera = target.get(CameraTargetComponent.class);
            final var config = target.get(CameraLeadComponent.class);
            final var velocity = target.get(PhysicsComponent.class).velocity;
            final var targetOffset = Vector.of(velocity.x() * config.xModifier, velocity.y() * config.yModifier);
            final var smoothedOffset = camera.offset.lerp(targetOffset, Percent.of(config.adjustmentSpeed * engine.loop().delta()));

            camera.offset = engine.graphics().viewport(camera.viewportId)
                .map(Viewport::canvas)
                .map(canvas -> Bounds.atOrigin(-canvas.width() / 4.0, -canvas.height() / 4.0, canvas.width() / 2.0, canvas.height() / 2.0).clamp(smoothedOffset))
                .orElse(smoothedOffset);
        }
    }
}
