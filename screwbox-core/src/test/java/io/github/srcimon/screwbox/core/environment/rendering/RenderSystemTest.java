package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class RenderSystemTest {

    @Captor
    ArgumentCaptor<SpriteBatch> spriteBatch;

    @Test
    void update_oneSpriteOnScreen_drawsSpriteBatchWithOneSprite(DefaultEnvironment environment, Camera camera, Canvas canvas, Graphics graphics) {
        var sprite = SpriteBundle.ICON.get();
        when(camera.zoom()).thenReturn(2.0);
        when(canvas.bounds()).thenReturn(new ScreenBounds(0, 0, 640, 480));
        when(graphics.toCanvas(Bounds.$$(176, 176, 48, 48), 1, 1)).thenReturn(new ScreenBounds(20, 20, 8, 8));
        when(graphics.visibleArea()).thenReturn(Bounds.$$(176, 176, 48, 48));

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
