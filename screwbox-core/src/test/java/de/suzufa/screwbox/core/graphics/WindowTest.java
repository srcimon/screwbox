package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.Offset.origin;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Percent;

@ExtendWith(MockitoExtension.class)
class WindowTest {

    @Spy
    Window window;

    @Test
    void fillWith_sprite_callsActualMethod() {
        Sprite sprite = sprite();

        window.fillWith(sprite);

        verify(window).fillWith(origin(), sprite, 1, Percent.max());
    }

    @Test
    void fillWith_pffsetAndSprite_callsActualMethod() {
        Sprite sprite = sprite();

        window.fillWith(Offset.at(2, 3), sprite);

        verify(window).fillWith(Offset.at(2, 3), sprite, 1, Percent.max());
    }

    @Test
    void drawLine_fromAndTo_callsActualMethod() {
        Offset from = Offset.at(2, 4);
        Offset to = Offset.at(1, 1);
        when(window.drawColor()).thenReturn(Color.BLUE);

        window.drawLine(from, to);

        verify(window).drawLine(from, to, Color.BLUE);
    }

    @Test
    void drawText_offsetTextAndFont_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        window.drawText(Offset.at(2, 4), "Test", font);

        verify(window).drawText(Offset.at(2, 4), "Test", font, Percent.max(), 1);
    }

    @Test
    void drawText_offsetTextFontAndScale_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        window.drawText(Offset.at(2, 4), "Test", font, 4);

        verify(window).drawText(Offset.at(2, 4), "Test", font, Percent.max(), 4);
    }

    @Test
    void drawText_offsetTextFontAndOpacity_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        window.drawText(Offset.at(2, 4), "Test", font, Percent.half());

        verify(window).drawText(Offset.at(2, 4), "Test", font, Percent.half(), 1);
    }

    @Test
    void drawTextCentered_offsetTextAndFont_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        window.drawTextCentered(Offset.at(2, 4), "Test", font);

        verify(window).drawTextCentered(Offset.at(2, 4), "Test", font, Percent.max(), 1);
    }

    @Test
    void drawTextCentered_offsetTextFontAndScale_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        window.drawTextCentered(Offset.at(2, 4), "Test", font, 4);

        verify(window).drawTextCentered(Offset.at(2, 4), "Test", font, Percent.max(), 4);
    }

    @Test
    void drawTextCentered_offsetTextFontAndOpacity_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        window.drawTextCentered(Offset.at(2, 4), "Test", font, Percent.half());

        verify(window).drawTextCentered(Offset.at(2, 4), "Test", font, Percent.half(), 1);
    }

    @Test
    void setCursor_predefinedCursor_setsFullscreenAndWindowCursor() {
        window.setCursor(MouseCursor.HIDDEN);

        verify(window).setWindowCursor(MouseCursor.HIDDEN);
        verify(window).setFullscreenCursor(MouseCursor.HIDDEN);
    }

    @Test
    void setCursor_multiImageSprite_exception() {
        Sprite multiImageSprite = new Sprite(List.of(Frame.invisible(), Frame.invisible()));

        assertThatThrownBy(() -> window.setCursor(multiImageSprite))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The sprite has more than one frame.");
    }

    @Test
    void setCursor_singleFrameSprite_exception() {
        Sprite sprite = Sprite.invisible();

        window.setCursor(sprite);

        verify(window).setFullscreenCursor(sprite.singleFrame());
        verify(window).setWindowCursor(sprite.singleFrame());
    }

    private Sprite sprite() {
        return Sprite.fromFile("tile.bmp");
    }
}
