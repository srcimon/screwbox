package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScreenTest {

    @Spy
    Screen screen;

    @Test
    void drawRectangle_usingBounds_drawsRectangleAtPositionWithSize() {

        screen.drawRectangle(new ScreenBounds(10, 4, 13, 20), RectangleDrawOptions.filled(Color.RED));

        verify(screen).drawRectangle(Offset.at(10, 4), Size.of(13, 20), RectangleDrawOptions.filled(Color.RED));
    }

    @Test
    void drawText_offsetTextAndFont_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        screen.drawText(Offset.at(2, 4), "Test", font);

        verify(screen).drawText(Offset.at(2, 4), "Test", font, Percent.max(), 1);
    }

    @Test
    void drawText_offsetTextFontAndScale_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        screen.drawText(Offset.at(2, 4), "Test", font, 4);

        verify(screen).drawText(Offset.at(2, 4), "Test", font, Percent.max(), 4);
    }

    @Test
    void drawText_offsetTextFontAndOpacity_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        screen.drawText(Offset.at(2, 4), "Test", font, Percent.half());

        verify(screen).drawText(Offset.at(2, 4), "Test", font, Percent.half(), 1);
    }

    @Test
    void drawTextCentered_offsetTextAndFont_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        screen.drawTextCentered(Offset.at(2, 4), "Test", font);

        verify(screen).drawTextCentered(Offset.at(2, 4), "Test", font, Percent.max(), 1);
    }

    @Test
    void drawTextCentered_offsetTextFontAndScale_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        screen.drawTextCentered(Offset.at(2, 4), "Test", font, 4);

        verify(screen).drawTextCentered(Offset.at(2, 4), "Test", font, Percent.max(), 4);
    }

    @Test
    void drawTextCentered_offsetTextFontAndOpacity_callsActualMethod() {
        Pixelfont font = Pixelfont.defaultFont(Color.WHITE);

        screen.drawTextCentered(Offset.at(2, 4), "Test", font, Percent.half());

        verify(screen).drawTextCentered(Offset.at(2, 4), "Test", font, Percent.half(), 1);
    }
}
