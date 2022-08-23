package de.suzufa.screwbox.core.entityengine.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.CameraComponent;
import de.suzufa.screwbox.core.entityengine.components.CameraMovementComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.loop.Metrics;
import de.suzufa.screwbox.test.extensions.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class CameraMovementSystemTest {

    @Test
    void update_movesCameraTowardsTracker(DefaultEntityEngine entityEngine, Metrics metrics, Window screen,
            Graphics graphics) {
        when(metrics.delta()).thenReturn(0.4);
        when(screen.size()).thenReturn(Dimension.of(640, 480));
        when(graphics.updateCameraZoom(anyDouble())).thenReturn(1.0);

        CameraMovementComponent cameraMovement = new CameraMovementComponent(1.5, 1239);
        cameraMovement.shift = Vector.yOnly(200);

        Entity camera = new Entity().add(
                new CameraComponent(2),
                cameraMovement,
                new TransformComponent(Bounds.atPosition(200, 500, 0, 0)));

        Entity tracked = new Entity(1239).add(
                new TransformComponent(Bounds.atPosition(1000, 0, 0, 0)));

        Entity worldBounds = new Entity().add(
                new WorldBoundsComponent(),
                new TransformComponent(Bounds.atPosition(0, 0, 20000, 20000)));

        entityEngine
                .add(camera, tracked, worldBounds)
                .add(new CameraMovementSystem());

        entityEngine.updateTimes(50);

        Vector cameraPosition = camera.get(TransformComponent.class).bounds.position();
        assertThat(cameraPosition).isEqualTo(Vector.of(1000, 200));
    }

}