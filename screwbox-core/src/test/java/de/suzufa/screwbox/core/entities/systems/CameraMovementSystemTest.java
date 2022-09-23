package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.CameraComponent;
import de.suzufa.screwbox.core.entities.components.CameraMovementComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.loop.Loop;
import de.suzufa.screwbox.core.test.EntitiesExtension;

@ExtendWith(EntitiesExtension.class)
class CameraMovementSystemTest {

    @Test
    void update_movesCameraTowardsTracker(DefaultEntities entities, Loop loop, Window screen,
            Graphics graphics) {
        when(loop.delta()).thenReturn(0.4);
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

        entities
                .add(camera, tracked, worldBounds)
                .add(new CameraMovementSystem());

        entities.updateTimes(50);

        Vector cameraPosition = camera.get(TransformComponent.class).bounds.position();
        assertThat(cameraPosition).isEqualTo(Vector.of(1000, 200));
    }

}