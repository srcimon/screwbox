package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class RenderSystemTest {

    @Captor
    ArgumentCaptor<SpriteBatch> spriteBatch;

    @Mock
    Viewport viewport;

    @Mock
    Camera camera;

    @Mock
    Canvas canvas;

    @Test
    void update_oneSpriteOnScreen_drawsSpriteBatchWithOneSprite(DefaultEnvironment environment, Graphics graphics) {
        var sprite = SpriteBundle.ICON.get();
        when(viewport.camera()).thenReturn(camera);
        when(viewport.canvas()).thenReturn(canvas);
        when(camera.zoom()).thenReturn(2.0);
        when(graphics.activeViewports()).thenReturn(List.of(viewport));
        when(viewport.toCanvas(Bounds.$$(176, 176, 48, 48), 1, 1)).thenReturn(new ScreenBounds(20, 20, 8, 8));
        when(viewport.visibleArea()).thenReturn(Bounds.$$(176, 176, 48, 48));
        when(canvas.size()).thenReturn(Size.of(800, 800));

        environment
                .addEntity(
                        new TransformComponent(200, 200, 16, 16),
                        new RenderComponent(sprite, 5))
                .addSystem(new RenderSystem());

        environment.update();

        verify(canvas).drawSpriteBatch(spriteBatch.capture());

        assertThat(spriteBatch.getValue().entriesInOrder()).containsExactly(
                new SpriteBatch.SpriteBatchEntry(sprite, Offset.at(20, 20), SpriteDrawOptions.scaled(2), 5));
    }

    @Test
    @Disabled
        //TODO FIXME
    void update_spriteOnTopOfLight_drawsNoSprite(DefaultEnvironment environment, Camera camera, Graphics graphics, Canvas canvas) {
        var sprite = SpriteBundle.ICON.get();
        when(camera.zoom()).thenReturn(2.0);
        when(canvas.bounds()).thenReturn(new ScreenBounds(0, 0, 640, 480));
        when(graphics.toCanvas(Bounds.$$(176, 176, 48, 48), 1, 1)).thenReturn(new ScreenBounds(20, 20, 8, 8));
        when(graphics.visibleArea()).thenReturn(Bounds.$$(176, 176, 48, 48));

        environment
                .addEntity(new Entity()
                        .add(new TransformComponent(200, 200, 16, 16))
                        .addCustomized(new RenderComponent(sprite, 5), render -> render.renderOverLight = true))
                .addSystem(new RenderSystem());

        environment.update();

        verify(canvas).drawSpriteBatch(spriteBatch.capture());

        assertThat(spriteBatch.getValue().isEmpty()).isTrue();
    }
}
