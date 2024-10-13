package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultRendererTest {

    @Mock
    Graphics2D graphics;

    @InjectMocks
    DefaultRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer.updateGraphicsContext(() -> graphics, Size.of(640, 480));
    }

    @Test
    void fillWith_newColor_changesColorAndFillsRect() {
        renderer.fillWith(Color.RED);

        verify(graphics).setColor(new java.awt.Color(255, 0, 0));
        verify(graphics, times(1)).fillRect(0, 0, 640, 480);
    }

    @Test
    void fillWith_sameColor_changesColorOnlyOnce() {
        renderer.fillWith(Color.RED);
        renderer.fillWith(Color.RED);

        verify(graphics).setColor(new java.awt.Color(255, 0, 0));
        verify(graphics, times(2)).fillRect(0, 0, 640, 480);
    }

    @Test
    void drawSpriteBatch_threSpritesInBatch_drawsAllSprites() {
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.add(Sprite.invisible(), Offset.at(10, 20), SpriteDrawOptions.originalSize(), 4);
        spriteBatch.add(Sprite.invisible(), Offset.at(20, 20), SpriteDrawOptions.originalSize(), 2);
        spriteBatch.add(Sprite.invisible(), Offset.at(30, 20), SpriteDrawOptions.originalSize(), 1);

        renderer.drawSpriteBatch(spriteBatch);

        verify(graphics, times(3)).drawImage(any(), any(), any());
    }
}
