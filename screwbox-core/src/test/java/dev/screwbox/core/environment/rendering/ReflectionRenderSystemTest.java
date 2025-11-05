package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class ReflectionRenderSystemTest {

    @Mock
    Viewport viewport;

    @Mock
    Camera camera;

    @Mock
    Canvas canvas;

    @Test
    void update_reflectionNotOnScreen_noReflectionDrawn(DefaultEnvironment environment, Graphics graphics) {
        when(viewport.camera()).thenReturn(camera);
        when(graphics.viewports()).thenReturn(List.of(viewport));
        when(graphics.configuration()).thenReturn(new GraphicsConfiguration());
        when(camera.zoom()).thenReturn(2.0);
        when(viewport.visibleArea()).thenReturn(Bounds.$$(0, 0, 640, 480));
        environment
                .addEntity(new TransformComponent(200, 200, 16, 16), new RenderComponent(SpriteBundle.ICON, 5))
                .addEntity(new TransformComponent(1200, 216, 400, 400), new ReflectionComponent(Percent.half(), 12))
                .addSystem(new ReflectionRenderSystem());

        environment.update();

        verifyNoMoreInteractions(canvas);
    }

    @Test
    void update_noReflections_noReflectionDrawn(DefaultEnvironment environment) {
        environment
                .addEntity(new TransformComponent(200, 200, 16, 16), new RenderComponent(SpriteBundle.ICON, 5))
                .addSystem(new ReflectionRenderSystem());

        environment.update();

        verifyNoMoreInteractions(canvas);
    }

    @Test
    void update_noRenderer_noReflectionDrawn(DefaultEnvironment environment) {
        environment
                .addEntity(new TransformComponent(1200, 216, 400, 400), new ReflectionComponent(Percent.half(), 12))
                .addSystem(new ReflectionRenderSystem());

        environment.update();

        verifyNoMoreInteractions(canvas);
    }
}
