package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.core.GlobalBoundsComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

@Order(SystemOrder.PREPARATION)
public class CameraUpdateSystem implements EntitySystem {

    private static final Archetype CAMERA = Archetype.of(TransformComponent.class, CameraComponent.class,
            CameraMovementComponent.class);
    private static final Archetype WORLD_BOUNDS = Archetype.of(GlobalBoundsComponent.class, TransformComponent.class);

    boolean first = true;

    @Override
    public void update(final Engine engine) {
        final Entity cameraEntity = engine.environment().forcedFetch(CAMERA);
        engine.graphics().updateZoom(cameraEntity.get(CameraComponent.class).zoom);

        final Bounds worldBounds = engine.environment().forcedFetch(WORLD_BOUNDS).get(TransformComponent.class).bounds;
        final var camBoundsComponent = cameraEntity.get(TransformComponent.class);
        final Vector cameraPosition = camBoundsComponent.bounds.position();
        final var cameraTrackerComponent = cameraEntity.get(CameraMovementComponent.class);
        final Vector trackerPosition = engine.environment().forcedFetchById(cameraTrackerComponent.trackedEntityId).get(TransformComponent.class).bounds.position();
        final double cameraTrackerSpeed = cameraTrackerComponent.speed;
        final double distX = cameraPosition.x() - trackerPosition.x() - cameraTrackerComponent.shift.x();
        final double distY = cameraPosition.y() - trackerPosition.y() - cameraTrackerComponent.shift.y();

        double delta = engine.loop().delta();

        if (first) {
            first = false;
            engine.graphics().setCameraPosition(trackerPosition);
        } else {
            Vector cameraMovement = Vector.$(
                    distX * -1 * cameraTrackerSpeed * delta,
                    distY * -1 * cameraTrackerSpeed * delta);

            Vector newCameraPosition = engine.graphics().moveCameraWithinVisualBounds(cameraMovement, worldBounds);
            camBoundsComponent.bounds = camBoundsComponent.bounds.moveTo(newCameraPosition);
        }
    }
}
