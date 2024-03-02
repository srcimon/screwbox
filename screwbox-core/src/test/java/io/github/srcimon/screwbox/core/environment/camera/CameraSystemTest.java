package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class CameraSystemTest {

    @Test
    void update_noCameraTarget_doesntMoveCamera(DefaultEnvironment environment, Camera camera) {
        environment.addSystem(new CameraSystem());

        environment.update();

        verify(camera, never()).moveWithinVisualBounds(any(), any());
        verify(camera, never()).move(any());
        verify(camera, never()).updatePosition(any());
    }

    @Test
    void update_firstUpdateWithTarget_movesCameraToTarget(DefaultEnvironment environment, Camera camera) {
        environment
                .addSystem(new CameraSystem())
                .addEntity(new CameraTargetComponent(), new TransformComponent(200, 300, 10, 10));

        environment.update();

        verify(camera).updatePosition($(200, 300));
    }

    @Test
    void update_secondUpdateWithTarget_movesCameraTowardsTarget(DefaultEnvironment environment, Camera camera, Loop loop) {
        when(loop.delta(-2)).thenReturn(0.4);
        when(camera.position()).thenReturn($(100, 100));
        environment
                .addSystem(new CameraSystem())
                .addEntity(new CameraTargetComponent(), new TransformComponent(200, 300, 10, 10));

        environment.updateTimes(2);

        verify(camera).move($(-40.00, -80.00));
    }

    @Test
    void update_secondUpdateWithTargetAndBounds_movesCameraTowardsTargetWithinBounds(DefaultEnvironment environment, Camera camera, Loop loop) {
        when(loop.delta(-2)).thenReturn(0.4);
        when(camera.position()).thenReturn($(100, 100));
        environment
                .addSystem(new CameraSystem())
                .addEntity(new CameraBoundsComponent($$(0, 0, 1000, 1000)))
                .addEntity(new CameraTargetComponent(), new TransformComponent(200, 300, 10, 10));

        environment.updateTimes(2);

        verify(camera).moveWithinVisualBounds($(-40.00, -80.00), $$(0, 0, 1000, 1000));
    }

}