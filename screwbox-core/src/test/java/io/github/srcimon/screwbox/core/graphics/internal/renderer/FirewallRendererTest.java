package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class FirewallRendererTest {

    private static final ScreenBounds CLIP = new ScreenBounds(0, 0, 640, 480);

    @InjectMocks
    FirewallRenderer renderer;

    @Mock
    Renderer next;

    @BeforeEach
    void setUp() {
        renderer.updateContext(() -> null);
        Mockito.reset(next);
    }

    @Test
    void fillWith_colorIsTransparent_skipsRendering() {
        renderer.fillWith(Color.TRANSPARENT, CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void fillWith_colorIsBlack_renders() {
        renderer.fillWith(Color.BLACK, CLIP);

        verify(next).fillWith(Color.BLACK, CLIP);
    }

    @Test
    void fillWith_spriteHasZeroOpacity_skipsRendering() {
        renderer.fillWith(Sprite.invisible(), SpriteFillOptions.scale(2).opacity(Percent.zero()), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void fillWith_spriteHasHalfOpacity_renders() {
        renderer.fillWith(Sprite.invisible(), SpriteFillOptions.scale(2).opacity(Percent.half()), CLIP);

        verify(next).fillWith(any(), any(), any());
    }

    @Test
    void drawText_alignedRightAndLeftOfScreen_skipsRendering() {
        renderer.drawText(Offset.at(-2, -4), "Test", SystemTextDrawOptions.systemFont("Arial").alignRight(), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawText_alignedLeftAndRightOfScreen_skipsRendering() {
        renderer.drawText(Offset.at(650, -4), "Test", SystemTextDrawOptions.systemFont("Arial"), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawText_transparent_skipsRendering() {
        renderer.drawText(Offset.at(0, 10), "Test", SystemTextDrawOptions.systemFont("Arial").color(Color.TRANSPARENT), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawText_noText_skipsRendering() {
        renderer.drawText(Offset.at(0, 10), "", SystemTextDrawOptions.systemFont("Arial"), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawText_blueText_renders() {
        renderer.drawText(Offset.at(0, 10), "Test", SystemTextDrawOptions.systemFont("Arial").color(Color.BLUE), CLIP);

        verify(next).drawText(any(), anyString(), any(SystemTextDrawOptions.class), any());
    }

    @Test
    void drawRectangle_invalidSize_skipsRendering() {
        renderer.drawRectangle(Offset.origin(), Size.of(0, 2), RectangleDrawOptions.filled(Color.BLUE), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawRectangle_transparent_skipsRendering() {
        renderer.drawRectangle(Offset.origin(), Size.of(4, 2), RectangleDrawOptions.filled(Color.TRANSPARENT), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawRectangle_outOfBounds_skipsRendering() {
        renderer.drawRectangle(Offset.at(1000, 40), Size.of(4, 2), RectangleDrawOptions.filled(Color.BLACK), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawRectangle_visibleRectangle_renders() {
        renderer.drawRectangle(Offset.at(70, 40), Size.of(4, 2), RectangleDrawOptions.filled(Color.BLACK), CLIP);

        verify(next).drawRectangle(any(), any(), any(), any());
    }

    @Test
    void drawLine_transparent_skipsRendering() {
        renderer.drawLine(Offset.origin(), Offset.at(20, 40), LineDrawOptions.color(Color.TRANSPARENT), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawLine_outOfBounds_skipsRendering() {
        renderer.drawLine(Offset.at(500, 1000), Offset.at(20, 4000), LineDrawOptions.color(Color.BLUE), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawLine_visibleLine_renders() {
        renderer.drawLine(Offset.origin(), Offset.at(20, 40), LineDrawOptions.color(Color.BLUE).strokeWidth(2), CLIP);

        verify(next).drawLine(any(), any(), any(), any());
    }

    @Test
    void drawCircle_visibleCircle_renders() {
        renderer.drawCircle(Offset.at(20, 40), 20, CircleDrawOptions.outline(Color.RED), CLIP);

        verify(next).drawCircle(any(), anyInt(), any(), any());
    }

    @Test
    void drawCircle_outOfBounds_skipsRendering() {
        renderer.drawCircle(Offset.at(2000, 40), 20, CircleDrawOptions.outline(Color.RED), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawCircle_transparent_skipsRendering() {
        renderer.drawCircle(Offset.at(20, 40), 20, CircleDrawOptions.outline(Color.TRANSPARENT), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawCircle_noRadius_skipsRendering() {
        renderer.drawCircle(Offset.at(20, 40), 0, CircleDrawOptions.outline(Color.BLACK), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawSprite_transparent_skipsRendering() {
        renderer.drawSprite(SpriteBundle.SMOKE.get(), Offset.origin(), SpriteDrawOptions.originalSize().opacity(0), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawSprite_visibleSprite_renders() {
        renderer.drawSprite(SpriteBundle.SMOKE.get(), Offset.origin(), SpriteDrawOptions.originalSize(), CLIP);

        verify(next).drawSprite(any(Sprite.class), any(), any(), any());
    }

    @Test
    void drawSprite_suppliedSpriteIsTransparent_skipsRendering() {
        renderer.drawSprite(SpriteBundle.SMOKE, Offset.origin(), SpriteDrawOptions.originalSize().opacity(0), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawSprite_visibleSuppliedSprite_renders() {
        renderer.drawSprite(SpriteBundle.SMOKE, Offset.origin(), SpriteDrawOptions.originalSize(), CLIP);

        verify(next).drawSprite(any(Supplier.class), any(), any(), any());
    }

    @Test
    void drawText_pixelfontNoText_skipsRendering() {
        renderer.drawText(Offset.origin(), "", TextDrawOptions.font(FontBundle.BOLDZILLA), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawText_pixelfontSizeZero_skipsRendering() {
        renderer.drawText(Offset.origin(), "Test", TextDrawOptions.font(FontBundle.BOLDZILLA).scale(0), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawText_pixelfontTransparent_skipsRendering() {
        renderer.drawText(Offset.origin(), "Test", TextDrawOptions.font(FontBundle.BOLDZILLA).opacity(Percent.zero()), CLIP);

        verifyNoInteractions(next);
    }

    @Test
    void drawText_pixelfontIsVisible_renders() {
        renderer.drawText(Offset.origin(), "Test", TextDrawOptions.font(FontBundle.BOLDZILLA), CLIP);

        verify(next).drawText(any(), any(), any(TextDrawOptions.class), any());
    }

    @Test
    void drawSpriteBatch_batchHasEntries_renders() {
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.add(SpriteBundle.DOT_YELLOW.get(), Offset.origin(), SpriteDrawOptions.originalSize(), 2);

        renderer.drawSpriteBatch(spriteBatch, CLIP);

        verify(next).drawSpriteBatch(any(), any());
    }

    @Test
    void drawSpriteBatch_batchIsEmpty_skipsRendering() {
        renderer.drawSpriteBatch(new SpriteBatch(), CLIP);

        verifyNoInteractions(next);
    }
}
