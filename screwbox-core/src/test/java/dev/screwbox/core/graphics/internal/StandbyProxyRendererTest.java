package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.internal.renderer.StandbyProxyRenderer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@MockitoSettings
class StandbyProxyRendererTest {

    private static final ScreenBounds CLIP = new ScreenBounds(0, 0, 640, 480);

    @Mock
    Renderer renderer;

    @InjectMocks
    StandbyProxyRenderer standbyProxyRenderer;

    @Test
    void fillWith_renderingActive_renders() {
        standbyProxyRenderer.toggleOnOff();

        standbyProxyRenderer.fillWith(Color.RED, CLIP);

        verify(renderer).fillWith(Color.RED, CLIP);
    }

    @Test
    void fillWith_renderingDisabled_doesntRender() {
        standbyProxyRenderer.fillWith(Color.RED, CLIP);

        verifyNoInteractions(renderer);
    }

    @Test
    void updateContext_notActive_noUpdate() {
        standbyProxyRenderer.updateContext(null);

        verifyNoInteractions(renderer);
    }

    @Test
    void updateContext_noFramesToSkip_updatesContext() {
        standbyProxyRenderer.toggleOnOff();
        standbyProxyRenderer.updateContext(null);

        verify(renderer).updateContext(null);
    }

    @Test
    void updateContext_skippingFrames_stillUpdates() {
        standbyProxyRenderer.toggleOnOff();
        standbyProxyRenderer.skipFrames(2);
        standbyProxyRenderer.updateContext(null);
        standbyProxyRenderer.updateContext(null);

        verify(renderer, times(2)).updateContext(any());
    }
}
