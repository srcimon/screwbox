package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Viewport;

//TODO document
public class CameraLeadSystem implements EntitySystem {

    private static final Archetype CAMERAS = Archetype.of(CameraLeadComponent.class, CameraTargetComponent.class, PhysicsComponent.class);
    //TODO finish up
    @Override
    public void update(final Engine engine) {
        for (final var camera : engine.environment().fetchAll(CAMERAS)) {
            final double delta = engine.loop().delta();
                var configuration = camera.get(CameraTargetComponent.class);
                Vector velocity = camera.get(PhysicsComponent.class).velocity;
                var targetV = Vector.of(velocity.x() * 1.0, velocity.y() * 0.25);
                var actzualV = configuration.offset.lerp(targetV, Percent.of(5 * delta));
                var viewport = engine.graphics().viewport(configuration.viewportId);

                configuration.offset = viewport
                    .map(Viewport::canvas)
                    .map(canvas -> Bounds.atOrigin(-canvas.width() / 4.0, -canvas.height() / 4.0, canvas.width() / 2.0, canvas.height() / 2.0).clamp(actzualV))
                    .orElse(actzualV);
            }
    }
}
