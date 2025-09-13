package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.mouse.Mouse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class CursorLockInSupportTest {

    @Mock
    Robot robot;

    @Mock
    Mouse mouse;

    @InjectMocks
    CursorLockInSupport cursorLockInSupport;

    @Test
    void lockInCursor_cursorWithinBounds_notTriggeringRobot() {
        when(mouse.offset()).thenReturn(Offset.at(120, 200));
        cursorLockInSupport.lockInCursor(new ScreenBounds(40, 90, 600, 400), 10);
        verifyNoInteractions(robot);
    }

    @ParameterizedTest
    @CsvSource({
            "8,200,50,290",
            "800,200,630,290",
            "200,2,240,100",
            "200,2000,240,480",
            "800,2000,630,480"
    })
    void lockInCursor_cursorOutOfBound_movesCursorBackIn(int x, int y, int targetX, int targetY) {
        when(mouse.offset()).thenReturn(Offset.at(x, y));

        cursorLockInSupport.lockInCursor(new ScreenBounds(40, 90, 600, 400), 10);

        verify(robot).mouseMove(targetX, targetY);
    }
}
