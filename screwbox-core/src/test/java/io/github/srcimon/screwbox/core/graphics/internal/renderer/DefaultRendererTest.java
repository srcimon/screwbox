package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.mockito.Mockito.*;

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

}
