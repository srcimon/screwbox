package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.StandbyProxyRenderer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

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
        standbyProxyRenderer.toggle();

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
        standbyProxyRenderer.toggle();
        standbyProxyRenderer.updateContext(null);

        verify(renderer).updateContext(null);
    }

    @Test
    void updateContext_skippingFrames_noUpdate() {
        standbyProxyRenderer.toggle();
        standbyProxyRenderer.skipFrames();
        standbyProxyRenderer.updateContext(null);
        standbyProxyRenderer.updateContext(null);

        verifyNoInteractions(renderer);
    }
}
