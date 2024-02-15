package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultRendererTest {

    @Mock
    WindowFrame frame;

    @Mock
    Graphics2D graphics;

    @InjectMocks
    DefaultRenderer renderer;

    @Test
    void fillWith_newColor_changesColorAndFillsRect() {
        when(frame.getWidth()).thenReturn(640);
        when(frame.getHeight()).thenReturn(480);
        renderer.fillWith(Color.RED);

        verify(graphics).setColor(new java.awt.Color(255, 0, 0));
        verify(graphics).fillRect(0, 0, 640, 480);
    }

    //TODO Test fillWith_sameColor_doesntChangeColorAgain()
}
