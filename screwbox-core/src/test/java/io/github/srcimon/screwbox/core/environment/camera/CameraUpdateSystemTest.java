package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class CameraUpdateSystemTest {
//TODO FIX Tests
//    @Test
//    void update_isFirstUpdate_initializesCameraPosition(DefaultEnvironment environment, Loop loop, Screen screen,
//                                                        Graphics graphics) {
//        when(loop.delta()).thenReturn(0.4);
//        when(screen.center()).thenReturn(Offset.at(320, 240));
//        when(graphics.updateZoom(anyDouble())).thenReturn(1.0);
//
//        Entity camera = new Entity().add(
//                new CameraComponent(2),
//                new CameraMovementComponent(1.5, 1239),
//                new TransformComponent(Bounds.atPosition(200, 500, 0, 0)));
//
//        Entity tracked = new Entity(1239).add(
//                new TransformComponent(Bounds.atPosition(1000, 0, 0, 0)));
//
//        Entity worldBounds = new Entity().add(
//                new CameraConfigurationComponent(),
//                new TransformComponent(Bounds.atPosition(0, 0, 20000, 20000)));
//
//        environment
//                .addEntities(camera, tracked, worldBounds)
//                .addSystem(new CameraUpdateSystem());
//
//        environment.update();
//
//        Vector cameraPosition = camera.get(TransformComponent.class).bounds.position();
//        assertThat(cameraPosition).isEqualTo(Vector.of(200, 500));
//    }
//
//    @Test
//    void update_anyFurtherUpdate_movesTrackerAndCameraTowardsTarget(DefaultEnvironment environment, Loop loop, Screen screen,
//                                                                    Graphics graphics) {
//        when(loop.delta()).thenReturn(0.4);
//        when(screen.center()).thenReturn(Offset.at(320, 240));
//        when(graphics.updateZoom(anyDouble())).thenReturn(1.0);
//
//        CameraMovementComponent cameraMovement = new CameraMovementComponent(1.5, 1239);
//        cameraMovement.shift = Vector.y(200);
//
//        Entity camera = new Entity().add(
//                new CameraComponent(2),
//                cameraMovement,
//                new TransformComponent(Bounds.atPosition(200, 500, 0, 0)));
//
//        Entity tracked = new Entity(1239).add(
//                new TransformComponent(Bounds.atPosition(1000, 0, 0, 0)));
//
//        Entity worldBounds = new Entity().add(
//                new CameraConfigurationComponent(),
//                new TransformComponent(Bounds.atPosition(0, 0, 20000, 20000)));
//
//        environment
//                .addEntities(camera, tracked, worldBounds)
//                .addSystem(new CameraUpdateSystem());
//
//        when(graphics.moveCameraWithinVisualBounds(Mockito.any(), Mockito.any())).thenReturn(Vector.$(100, 100));
//        environment.updateTimes(2);
//
//        Vector cameraPosition = camera.get(TransformComponent.class).bounds.position();
//        assertThat(cameraPosition).isEqualTo(Vector.of(100, 100));
//    }

}