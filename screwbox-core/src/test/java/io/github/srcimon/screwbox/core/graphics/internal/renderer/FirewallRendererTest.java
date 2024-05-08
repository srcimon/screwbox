package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class FirewallRendererTest {

    @InjectMocks
    FirewallRenderer renderer;

    @Mock
    Renderer next;

    @BeforeEach
    void setUp() {
        renderer.updateGraphicsContext(() -> null, Size.of(640, 480));
        Mockito.reset(next);
    }

    @Test
    void fillWith_colorIsTransparent_skipsRendering() {
        renderer.fillWith(Color.TRANSPARENT);

        verifyNoInteractions(next);
    }

    @Test
    void fillWith_colorIsBlack_renders() {
        renderer.fillWith(Color.BLACK);

        verify(next).fillWith(Color.BLACK);
    }

    @Test
    void fillWith_spriteHasZeroOpacity_skipsRendering() {
        renderer.fillWith(Sprite.invisible(), SpriteFillOptions.scale(2).opacity(Percent.zero()));

        verifyNoInteractions(next);
    }

    @Test
    void fillWith_spriteHasHalfOpacity_renders() {
        renderer.fillWith(Sprite.invisible(), SpriteFillOptions.scale(2).opacity(Percent.half()));

        verify(next).fillWith(any(), any());
    }

    @Test
    void drawText_transparent_skipsRendering() {
        renderer.drawText(Offset.at(0, 10), "Test", SystemTextDrawOptions.systemFont("Arial").color(Color.TRANSPARENT));

        verifyNoInteractions(next);
    }

    @Test
    void drawText_noText_skipsRendering() {
        renderer.drawText(Offset.at(0, 10), "", SystemTextDrawOptions.systemFont("Arial"));

        verifyNoInteractions(next);
    }

    @Test
    void drawText_blueText_renders() {
        renderer.drawText(Offset.at(0, 10), "Test", SystemTextDrawOptions.systemFont("Arial").color(Color.BLUE));

        verify(next).drawText(any(), anyString(), any(SystemTextDrawOptions.class));
    }

    @Test
    void drawRectangle_invalidSize_skipsRendering() {
        renderer.drawRectangle(Offset.origin(), Size.of(0, 2), RectangleDrawOptions.filled(Color.BLUE));

        verifyNoInteractions(next);
    }

    @Test
    void drawRectangle_transparent_skipsRendering() {
        renderer.drawRectangle(Offset.origin(), Size.of(4, 2), RectangleDrawOptions.filled(Color.TRANSPARENT));

        verifyNoInteractions(next);
    }

    @Test
    void drawRectangle_outOfBounds_skipsRendering() {
        renderer.drawRectangle(Offset.at(1000, 40), Size.of(4, 2), RectangleDrawOptions.filled(Color.BLACK));

        verifyNoInteractions(next);
    }

    @Test
    void drawRectangle_visibleRectangle_renders() {
        renderer.drawRectangle(Offset.at(70, 40), Size.of(4, 2), RectangleDrawOptions.filled(Color.BLACK));

        verify(next).drawRectangle(any(), any(), any());
    }

    @Test
    void drawLine_transparent_skipsRendering() {
        renderer.drawLine(Offset.origin(), Offset.at(20, 40), LineDrawOptions.color(Color.TRANSPARENT));

        verifyNoInteractions(next);
    }

    @Test
    void drawLine_strokeWidthZero_skipsRendering() {
        renderer.drawLine(Offset.origin(), Offset.at(20, 40), LineDrawOptions.color(Color.BLUE).strokeWidth(0));

        verifyNoInteractions(next);
    }

    @Test
    void drawLine_visibleLine_renders() {
        renderer.drawLine(Offset.origin(), Offset.at(20, 40), LineDrawOptions.color(Color.BLUE).strokeWidth(2));

        verify(next).drawLine(any(), any(), any());
    }

    @Test
    void drawCircle_transparent_skipsRendering() {
        renderer.drawCircle(Offset.at(20, 40), 20, CircleDrawOptions.outline(Color.TRANSPARENT));

        verifyNoInteractions(next);
    }

    @Test
    void drawCircle_noRadius_skipsRendering() {
        renderer.drawCircle(Offset.at(20, 40), 0, CircleDrawOptions.outline(Color.BLACK));

        verifyNoInteractions(next);
    }

    @Test
    void drawSprite_transparent_skipsRendering() {
        renderer.drawSprite(SpriteBundle.SMOKE_16.get(), Offset.origin(), SpriteDrawOptions.originalSize().opacity(0));

        verifyNoInteractions(next);
    }

    @Test
    void drawSprite_visibleSprite_renders() {
        renderer.drawSprite(SpriteBundle.SMOKE_16.get(), Offset.origin(), SpriteDrawOptions.originalSize());

        verify(next).drawSprite(any(Sprite.class), any(), any());
    }

    @Test
    void drawSprite_suppliedSpriteIsTransparent_skipsRendering() {
        renderer.drawSprite(SpriteBundle.SMOKE_16, Offset.origin(), SpriteDrawOptions.originalSize().opacity(0));

        verifyNoInteractions(next);
    }

    @Test
    void drawSprite_visibleSuppliedSprite_renders() {
        renderer.drawSprite(SpriteBundle.SMOKE_16, Offset.origin(), SpriteDrawOptions.originalSize());

        verify(next).drawSprite(any(Supplier.class), any(), any());
    }

    @Test
    void drawText_pixelfontNoText_skipsRendering() {
        renderer.drawText(Offset.origin(), "", TextDrawOptions.font(FontBundle.BOLDZILLA));

        verifyNoInteractions(next);
    }

    @Test
    void drawText_pixelfontSizeZero_skipsRendering() {
        renderer.drawText(Offset.origin(), "", TextDrawOptions.font(FontBundle.BOLDZILLA).scale(0));

        verifyNoInteractions(next);
    }

    @Test
    void drawText_pixelfontTransparent_skipsRendering() {
        renderer.drawText(Offset.origin(), "", TextDrawOptions.font(FontBundle.BOLDZILLA).opacity(Percent.zero()));

        verifyNoInteractions(next);
    }
}
