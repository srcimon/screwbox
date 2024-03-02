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
        engine.environment().tryFetchSingleton(TARGET).ifPresent(targetEntity -> {
            final var targetPosition = targetEntity.get(TransformComponent.class).bounds.position();
            final var target = targetEntity.get(CameraTargetComponent.class);

            if (mustInitializeCamera) {
                engine.graphics().camera().updatePosition(targetPosition);
                mustInitializeCamera = false;
                return;
            }

            final Vector cameraMovement = engine.graphics().camera().position()
                    .substract(targetPosition)
                    .substract(target.shift)
                    .multiply(engine.loop().delta(-1 * target.followSpeed));

            var configuration = engine.environment().tryFetchSingletonComponent(CameraBoundsComponent.class);
            if (configuration.isPresent()) {
                engine.graphics().camera().moveWithinVisualBounds(cameraMovement, configuration.get().cameraBounds);
            } else {
                engine.graphics().camera().move(cameraMovement);
            }
        });
    }
}
