package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

@Order(SystemOrder.PREPARATION)
public class CameraUpdateSystem implements EntitySystem {

    private static final Archetype CAMERA = Archetype.of(
            CameraMovementComponent.class);

    boolean first = true;

    @Override
    public void update(final Engine engine) {
        var camera = engine.environment().fetchSingletonComponent(CameraComponent.class);
        final Entity cameraEntity = engine.environment().fetchSingleton(CAMERA);
        engine.graphics().updateZoom(camera.zoom);

        final Vector cameraPosition = engine.graphics().cameraPosition();
        final var cameraMovementComponent = cameraEntity.get(CameraMovementComponent.class);
        final Vector trackerPosition = engine.environment().fetchById(cameraMovementComponent.trackedEntityId).get(TransformComponent.class).bounds.position();
        final double cameraTrackerSpeed = cameraMovementComponent.speed;
        final double distX = cameraPosition.x() - trackerPosition.x() - cameraMovementComponent.shift.x();
        final double distY = cameraPosition.y() - trackerPosition.y() - cameraMovementComponent.shift.y();

        double delta = engine.loop().delta();

        if (first) {
            first = false;
            engine.graphics().updateCameraPosition(trackerPosition);
        } else {
            Vector cameraMovement = Vector.$(
                    distX * -1 * cameraTrackerSpeed * delta,
                    distY * -1 * cameraTrackerSpeed * delta);

            engine.graphics().moveCameraWithinVisualBounds(cameraMovement, camera.visibleArea);
        }
    }
}
