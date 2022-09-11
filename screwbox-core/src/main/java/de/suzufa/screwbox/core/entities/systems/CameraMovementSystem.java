package de.suzufa.screwbox.core.entities.systems;

import static de.suzufa.screwbox.core.utils.MathUtil.clamp;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.CameraComponent;
import de.suzufa.screwbox.core.entities.components.CameraMovementComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;

public class CameraMovementSystem implements EntitySystem {

    private static final Archetype CAMERA = Archetype.of(TransformComponent.class, CameraComponent.class,
            CameraMovementComponent.class);
    private static final Archetype WORLD_BOUNDS = Archetype.of(WorldBoundsComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final Entity camera = engine.entityEngine().forcedFetch(CAMERA);
        double wantedZoom = camera.get(CameraComponent.class).zoom;
        double zoom = engine.graphics().updateCameraZoom(wantedZoom);

        final Bounds worldBounds = engine.entityEngine().forcedFetch(WORLD_BOUNDS)
                .get(TransformComponent.class).bounds;
        final var camBoundsComponent = camera.get(TransformComponent.class);
        final Vector cameraPosition = camBoundsComponent.bounds.position();
        final var cameraTrackerComponent = camera.get(CameraMovementComponent.class);
        final Vector trackerPosition = engine.entityEngine().forcedFetchById(cameraTrackerComponent.trackedEntityId)
                .get(TransformComponent.class).bounds.position();
        final double cameraTrackerSpeed = cameraTrackerComponent.speed;
        final double distX = cameraPosition.x() - trackerPosition.x() - cameraTrackerComponent.shift.x();
        final double distY = cameraPosition.y() - trackerPosition.y() - cameraTrackerComponent.shift.y();
        final Vector centerExtents = Vector.of(
                engine.graphics().window().size().width() / zoom / 2.0,
                engine.graphics().window().size().height() / zoom / 2.0);

        double delta = engine.loop().delta();
        final double movementX = clamp(
                worldBounds.minX() + centerExtents.x() - cameraPosition.x(),
                distX * -1 * cameraTrackerSpeed * delta,
                worldBounds.maxX() - cameraPosition.x() - centerExtents.x());

        final double movementY = clamp(
                worldBounds.minY() + centerExtents.y() - cameraPosition.y(),
                distY * -1 * cameraTrackerSpeed * delta,
                worldBounds.maxY() - cameraPosition.y() - centerExtents.y());

        final var updatedBounds = camBoundsComponent.bounds.moveBy(movementX, movementY);
        camBoundsComponent.bounds = updatedBounds;

        engine.graphics().updateCameraPosition(updatedBounds.position());
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PREPARATION;
    }
}