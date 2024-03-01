package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

@Order(SystemOrder.PREPARATION)
public class CameraSystem implements EntitySystem {

    private boolean first  = true;
    @Override
    public void update(final Engine engine) {
        var configuration = engine.environment().fetchSingletonComponent(CameraConfigurationComponent.class);

        final Vector cameraPosition = engine.graphics().cameraPosition();
        final var cameraMovementComponent = engine.environment().fetchSingletonComponent(CameraMovementComponent.class);
        final Vector trackerPosition = engine.environment().fetchById(cameraMovementComponent.trackedEntityId).get(TransformComponent.class).bounds.position();
        final double cameraTrackerSpeed = cameraMovementComponent.speed;
        final double distX = cameraPosition.x() - trackerPosition.x() - cameraMovementComponent.shift.x();
        final double distY = cameraPosition.y() - trackerPosition.y() - cameraMovementComponent.shift.y();

        double delta = engine.loop().delta();

        if (first) {
            engine.graphics().updateCameraPosition(trackerPosition);
            first = false;
        } else {
            Vector cameraMovement = Vector.$(
                    distX * -1 * cameraTrackerSpeed * delta,
                    distY * -1 * cameraTrackerSpeed * delta);

            engine.graphics().moveCameraWithinVisualBounds(cameraMovement, configuration.visibleArea);
        }
    }
}
