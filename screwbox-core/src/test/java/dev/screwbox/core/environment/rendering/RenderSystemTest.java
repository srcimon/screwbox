package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static dev.screwbox.core.Bounds.$$;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class RenderSystemTest {

    @Mock
    Viewport viewport;

    @Mock
    Camera camera;

    @Mock
    Canvas canvas;

    @BeforeEach
    void setUp(Graphics graphics) {
        when(viewport.camera()).thenReturn(camera);
        when(viewport.canvas()).thenReturn(canvas);
        when(graphics.viewports()).thenReturn(List.of(viewport));
    }

    @Test
    void update_oneSpriteOnScreen_drawsSprite(DefaultEnvironment environment) {
        var sprite = SpriteBundle.ICON.get();
        when(camera.zoom()).thenReturn(2.0);
        when(viewport.toCanvas($$(184, 184, 32, 32), 1, 1)).thenReturn(new ScreenBounds(20, 20, 8, 8));
        when(canvas.size()).thenReturn(Size.of(800, 800));

        environment
                .addEntity(new TransformComponent(200, 200, 16, 16), new RenderComponent(sprite, 5))
                .addSystem(new RenderSystem());

        environment.update();

        verify(canvas).drawSprite(sprite, Offset.at(20, 20), SpriteDrawOptions.scaled(2).drawOrder(5));
    }

//    @Test
//    void update_spriteOnTopOfLight_drawsNoSprite(DefaultEnvironment environment) {
//        var sprite = SpriteBundle.ICON.get();
//        when(camera.zoom()).thenReturn(2.0);
//
//        when(viewport.visibleArea()).thenReturn($$(176, 176, 48, 48));
//
//        environment
//                .addEntity(new Entity()
//                        .add(new TransformComponent(200, 200, 16, 16))
//                        .add(new RenderComponent(sprite, 5), render -> render.renderInForeground = true))
//                .addSystem(new RenderSystem());
//
//        environment.update();
//
//        verify(canvas).drawSpriteBatch(spriteBatch.capture());
//
//        assertThat(spriteBatch.getValue().isEmpty()).isTrue();
//    }
}
