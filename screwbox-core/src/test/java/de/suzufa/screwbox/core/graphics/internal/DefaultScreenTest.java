package de.suzufa.screwbox.core.graphics.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.awt.Rectangle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.graphics.Offset;

@ExtendWith(MockitoExtension.class)
class DefaultScreenTest {

    @InjectMocks
    DefaultScreen screen;

    @Mock
    Renderer renderer;

    @Mock
    WindowFrame frame;

    @Test
    void position_returnsScreenPosition() {
        when(frame.getBounds()).thenReturn(new Rectangle(40, 30, 1024, 768));
        when(frame.canvasHeight()).thenReturn(600);

        assertThat(screen.position()).isEqualTo(Offset.at(40, 198));
    }
}
