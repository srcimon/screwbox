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
        final var targetPosition = engine.environment().fetchSingleton(TARGET).get(TransformComponent.class).bounds.position();

        if (mustInitializeCamera) {
            engine.graphics().camera().updatePosition(targetPosition);
            mustInitializeCamera = false;
        } else {
            var configuration = engine.environment().fetchSingletonComponent(CameraConfigurationComponent.class);
            final Vector cameraPosition = engine.graphics().camera().position();

            final Vector cameraMovement = cameraPosition
                    .substract(targetPosition)
                    .substract(configuration.shift)
                    .multiply(-1 * configuration.speed * engine.loop().delta());

            engine.graphics().camera().moveWithinVisualBounds(cameraMovement, configuration.visibleArea);
        }
    }
}
