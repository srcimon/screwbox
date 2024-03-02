package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
    void update_firstUpdateWithCameraTargetPresent_movesCameraToTarget(DefaultEnvironment environment, Camera camera) {
        environment
                .addSystem(new CameraSystem())
                .addEntity(new CameraTargetComponent(), new TransformComponent(200, 300, 10, 10));

        environment.update();

        verify(camera).updatePosition(Vector.$(200, 300));
    }

}