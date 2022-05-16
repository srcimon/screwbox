package de.suzufa.screwbox.core.graphics.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.loop.Metrics;

@ExtendWith(MockitoExtension.class)
public class DefaultWindowTest {

    @InjectMocks
    DefaultWindow window;

    @Mock
    WindowFrame frame;

    @Mock
    GraphicsConfiguration configuration;

    @Mock
    Metrics metrics;

    @Test
    void drawColor_setsDrawColor() {
        window.drawColor(Color.WHITE);

        assertThat(window.drawColor()).isEqualTo(Color.WHITE);
    }
}
