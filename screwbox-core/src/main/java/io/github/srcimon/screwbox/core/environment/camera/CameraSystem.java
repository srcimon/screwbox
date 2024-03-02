package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

@Order(SystemOrder.PREPARATION)
public class CameraSystem implements EntitySystem {

    private static final Archetype TARGET = Archetype.of(CameraTargetComponent.class, TransformComponent.class);

    private boolean mustInitializeCamera = true;

    @Override
    public void update(final Engine engine) {
        var configuration = engine.environment().fetchSingletonComponent(CameraConfigurationComponent.class);

        final Vector cameraPosition = engine.graphics().camera().position();
        final var targetPosition = engine.environment().fetchSingleton(TARGET).get(TransformComponent.class).bounds.position();

        final double distX = cameraPosition.x() - targetPosition.x() - configuration.shift.x();
        final double distY = cameraPosition.y() - targetPosition.y() - configuration.shift.y();

        if (mustInitializeCamera) {
            engine.graphics().camera().updatePosition(targetPosition);
            mustInitializeCamera = false;
        } else {
            Vector cameraMovement = Vector.$(
                    distX * -1 * configuration.speed * engine.loop().delta(),
                    distY * -1 * configuration.speed * engine.loop().delta());

            engine.graphics().camera().moveWithinVisualBounds(cameraMovement, configuration.visibleArea);
        }
    }
}
