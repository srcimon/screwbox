package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultScreenTest {

    @InjectMocks
    DefaultScreen screen;

    @Mock
    WindowFrame frame;

    @Mock
    Renderer renderer;

    @Test
    void fillWith_callsRendererFillWith() {
        screen.fillWith(Color.BLUE);

        verify(renderer).fillWith(Color.BLUE);
    }
    @Test
    void position_returnsScreenPosition() {
        when(frame.getBounds()).thenReturn(new Rectangle(40, 30, 1024, 768));
        when(frame.canvasHeight()).thenReturn(600);

        assertThat(screen.position()).isEqualTo(Offset.at(40, 198));
    }

    @Test
    void isVisible_insideCanvas_isTrue() {
        when(frame.getCanvasSize()).thenReturn(Size.of(300, 400));

        assertThat(screen.isVisible(Offset.at(20, 40))).isTrue();
    }

    @Test
    void isVisible_outsideCanvas_isFalse() {
        when(frame.getCanvasSize()).thenReturn(Size.of(300, 400));

        assertThat(screen.isVisible(Offset.at(-20, 40))).isFalse();
    }
}
