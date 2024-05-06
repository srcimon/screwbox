package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
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

}
