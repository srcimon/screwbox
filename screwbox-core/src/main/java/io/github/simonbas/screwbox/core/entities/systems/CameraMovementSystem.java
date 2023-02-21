package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.CameraComponent;
import io.github.simonbas.screwbox.core.entities.components.CameraMovementComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.entities.components.WorldBoundsComponent;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.utils.MathUtil;

@Order(SystemOrder.PREPARATION)
public class CameraMovementSystem implements EntitySystem {

    private static final Archetype CAMERA = Archetype.of(TransformComponent.class, CameraComponent.class,
            CameraMovementComponent.class);
    private static final Archetype WORLD_BOUNDS = Archetype.of(WorldBoundsComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final Entity cameraEntity = engine.entities().forcedFetch(CAMERA);
        double wantedZoom = cameraEntity.get(CameraComponent.class).zoom;
        double zoom = engine.graphics().updateCameraZoom(wantedZoom);

        final Bounds worldBounds = engine.entities().forcedFetch(WORLD_BOUNDS)
                .get(TransformComponent.class).bounds;
        final var camBoundsComponent = cameraEntity.get(TransformComponent.class);
        final Vector cameraPosition = camBoundsComponent.bounds.position();
        final var cameraTrackerComponent = cameraEntity.get(CameraMovementComponent.class);
        final Vector trackerPosition = engine.entities().forcedFetchById(cameraTrackerComponent.trackedEntityId)
                .get(TransformComponent.class).bounds.position();
        final double cameraTrackerSpeed = cameraTrackerComponent.speed;
        final double distX = cameraPosition.x() - trackerPosition.x() - cameraTrackerComponent.shift.x();
        final double distY = cameraPosition.y() - trackerPosition.y() - cameraTrackerComponent.shift.y();
        final Offset screenCenter = engine.graphics().screen().center();
        final Vector centerExtents = Vector.of(screenCenter.x() / zoom, screenCenter.y() / zoom);

        double delta = engine.loop().delta();
        final double movementX = MathUtil.clamp(
                worldBounds.minX() + centerExtents.x() - cameraPosition.x(),
                distX * -1 * cameraTrackerSpeed * delta,
                worldBounds.maxX() - cameraPosition.x() - centerExtents.x());

        final double movementY = MathUtil.clamp(
                worldBounds.minY() + centerExtents.y() - cameraPosition.y(),
                distY * -1 * cameraTrackerSpeed * delta,
                worldBounds.maxY() - cameraPosition.y() - centerExtents.y());

        final var updatedBounds = camBoundsComponent.bounds.moveBy(movementX, movementY);
        camBoundsComponent.bounds = updatedBounds;

        engine.graphics().updateCameraPosition(updatedBounds.position());
    }
}