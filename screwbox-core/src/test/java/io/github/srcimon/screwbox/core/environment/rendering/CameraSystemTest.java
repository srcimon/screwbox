package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class CameraSystemTest {

    @Mock
    Viewport viewport;

    @Mock
    Camera camera;

    @Test
    void update_noCameraTarget_doesntMoveCamera(DefaultEnvironment environment, Camera camera) {
        environment.addSystem(new CameraSystem());

        environment.update();

        verify(camera, never()).moveWithinVisualBounds(any(), any());
        verify(camera, never()).move(any());
        verify(camera, never()).setPosition(any());
    }

    @Test
    void update_withTarget_movesCameraTowardsTarget(DefaultEnvironment environment, Loop loop, Graphics graphics) {
        when(graphics.vieport(0)).thenReturn(Optional.of(viewport));
        when(graphics.activeViewports()).thenReturn(List.of(viewport));
        when(viewport.camera()).thenReturn(camera);
        when(viewport.visibleArea()).thenReturn($$(0, 0, 640, 480));
        when(loop.delta(-2)).thenReturn(0.4);
        when(camera.position()).thenReturn($(100, 100));
        environment
                .addSystem(new CameraSystem())
                .addEntity(new CameraTargetComponent(), new TransformComponent(200, 300, 10, 10));

        environment.update();

        verify(camera).move($(-40.00, -80.00));
    }

    @Test
    void update_withTargetAndBounds_movesCameraTowardsTargetWithinBounds(DefaultEnvironment environment, Loop loop, Graphics graphics) {
        when(graphics.vieport(0)).thenReturn(Optional.of(viewport));
        when(graphics.activeViewports()).thenReturn(List.of(viewport));
        when(viewport.camera()).thenReturn(camera);
        when(viewport.visibleArea()).thenReturn($$(0, 0, 640, 480));
        when(loop.delta(-2)).thenReturn(0.4);
        when(camera.position()).thenReturn($(100, 100));
        environment
                .addSystem(new CameraSystem())
                .addEntity(new CameraBoundsComponent($$(0, 0, 1000, 1000)))
                .addEntity(new CameraTargetComponent(), new TransformComponent(200, 300, 10, 10));

        environment.update();

        verify(camera).moveWithinVisualBounds($(-40.00, -80.00), $$(0, 0, 1000, 1000));
    }

    @Test
    void update_tooFarAway_movesCameraDirectlyToTarge(DefaultEnvironment environment, Graphics graphics) {
        when(graphics.vieport(0)).thenReturn(Optional.of(viewport));
        when(graphics.activeViewports()).thenReturn(List.of(viewport));
        when(viewport.camera()).thenReturn(camera);
        when(viewport.visibleArea()).thenReturn($$(0, 0, 640, 480));
        when(camera.position()).thenReturn($(10000, 10000));
        environment
                .addSystem(new CameraSystem())
                .addEntity(new CameraBoundsComponent($$(0, 0, 1000, 1000)))
                .addEntity(new CameraTargetComponent(), new TransformComponent(200, 300, 10, 10));

        environment.update();

        verify(camera).setPosition($(200, 300));
    }

    @Test
    void update_tooFarAwayButJumpingNotAllowed_movesCameraTowardsTargetWithinBounds(DefaultEnvironment environment, Graphics graphics) {
        when(graphics.vieport(0)).thenReturn(Optional.of(viewport));
        when(graphics.activeViewports()).thenReturn(List.of(viewport));
        when(viewport.camera()).thenReturn(camera);
        when(camera.position()).thenReturn($(10000, 10000));
        CameraTargetComponent target = new CameraTargetComponent();
        target.allowJumping = false;

        environment
                .addSystem(new CameraSystem())
                .addEntity(new CameraBoundsComponent($$(0, 0, 1000, 1000)))
                .addEntity(target, new TransformComponent(200, 300, 10, 10));

        environment.update();

        verify(camera).moveWithinVisualBounds($(0, 0), $$(0, 0, 1000, 1000));
    }
}