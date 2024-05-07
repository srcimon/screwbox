package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FirewallRendererTest {

    @InjectMocks
    FirewallRenderer renderer;

    @Mock
    Renderer next;

    @BeforeEach
    void setUp() {
        renderer.updateGraphicsContext(() -> null, Size.of(640, 480));
    }

    @Test
    void fillWith_colorIsTransparent_skipsRendering() {
        renderer.fillWith(Color.TRANSPARENT);

        verify(next, never()).fillWith(any());
    }

    @Test
    void fillWith_colorIsBlack_renders() {
        renderer.fillWith(Color.BLACK);

        verify(next).fillWith(Color.BLACK);
    }

    @Test
    void fillWith_spriteHasZeroOpacity_skipsRendering() {
        renderer.fillWith(Sprite.invisible(), SpriteFillOptions.scale(2).opacity(Percent.zero()));

        verify(next, never()).fillWith(any(), any());
    }

    @Test
    void fillWith_spriteHasHalfOpacity_renders() {
        renderer.fillWith(Sprite.invisible(), SpriteFillOptions.scale(2).opacity(Percent.half()));

        verify(next).fillWith(any(), any());
    }

    @Test
    void drawText_transparent_skipsRendering() {
        renderer.drawText(Offset.at(0, 10), "Test", SystemTextDrawOptions.systemFont("Arial").color(Color.TRANSPARENT));

        verify(next, never()).drawText(any(), anyString(), any(SystemTextDrawOptions.class));
    }

    @Test
    void drawText_noText_skipsRendering() {
        renderer.drawText(Offset.at(0, 10), "", SystemTextDrawOptions.systemFont("Arial"));

        verify(next, never()).drawText(any(), anyString(), any(SystemTextDrawOptions.class));
    }

    @Test
    void drawText_blueText_renders() {
        renderer.drawText(Offset.at(0, 10), "Test", SystemTextDrawOptions.systemFont("Arial").color(Color.BLUE));

        verify(next).drawText(any(), anyString(), any(SystemTextDrawOptions.class));
    }

    @Test
    void drawRectangle_invalidSize_skipsRendering() {
        renderer.drawRectangle(Offset.origin(), Size.of(0, 2), RectangleDrawOptions.filled(Color.BLUE));

        verify(next, never()).drawRectangle(any(), any(), any());
    }

    @Test
    void drawRectangle_transparent_skipsRendering() {
        renderer.drawRectangle(Offset.origin(), Size.of(4, 2), RectangleDrawOptions.filled(Color.TRANSPARENT));

        verify(next, never()).drawRectangle(any(), any(), any());
    }

    @Test
    void drawRectangle_outOfBounds_skipsRendering() {
        renderer.drawRectangle(Offset.at(1000, 40), Size.of(4, 2), RectangleDrawOptions.filled(Color.BLACK));

        verify(next, never()).drawRectangle(any(), any(), any());
    }

    @Test
    void drawRectangle_validDrawingTasks_renders() {
        renderer.drawRectangle(Offset.at(70, 40), Size.of(4, 2), RectangleDrawOptions.filled(Color.BLACK));

        verify(next).drawRectangle(any(), any(), any());
    }
}
