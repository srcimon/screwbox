package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.Offset.origin;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Percent;

@ExtendWith(MockitoExtension.class)
class ScreenTest {

    @Spy
    Screen screen;

    @Test
    void fillWith_sprite_callsActualMethod() {
        Sprite sprite = sprite();

        screen.fillWith(sprite);

        verify(screen).fillWith(origin(), sprite, 1, Percent.max());
    }

    @Test
    void fillWith_pffsetAndSprite_callsActualMethod() {
        Sprite sprite = sprite();

        screen.fillWith(Offset.at(2, 3), sprite);

        verify(screen).fillWith(Offset.at(2, 3), sprite, 1, Percent.max());
    }

    @Test
    void drawLine_fromAndTo_callsActualMethod() {
        Offset from = Offset.at(2, 4);
        Offset to = Offset.at(1, 1);
        when(screen.drawColor()).thenReturn(Color.BLUE);

        screen.drawLine(from, to);

        verify(screen).drawLine(from, to, Color.BLUE);
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

    private Sprite sprite() {
        return Sprite.fromFile("tile.bmp");
    }
}
