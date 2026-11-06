package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.renderer.StandbyProxyRenderer;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.TextureFillOptions;
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
    void fillWith_textureRenderingDisabled_doesntRender() {
        standbyProxyRenderer.fillWith(SpriteBundle.BOX.get(), TextureFillOptions.scale(1), CLIP);

        verifyNoInteractions(renderer);
    }

    @Test
    void drawRectangle_renderingDisabled_doesntRender() {
        standbyProxyRenderer.drawRectangle(Offset.at(10, 2), Size.of(10, 20), RectangleDrawOptions.fading(Color.RED), CLIP);

        verifyNoInteractions(renderer);
    }

    @Test
    void drawRectangle_renderingEnabled_renders() {
        standbyProxyRenderer.toggleOnOff(); // on
        standbyProxyRenderer.toggleOnOff(); // off
        standbyProxyRenderer.toggleOnOff(); // on again

        standbyProxyRenderer.drawRectangle(Offset.at(10, 2), Size.of(10, 20), RectangleDrawOptions.fading(Color.RED), CLIP);

        verify(renderer).drawRectangle(Offset.at(10, 2), Size.of(10, 20), RectangleDrawOptions.fading(Color.RED), CLIP);
    }

    @Test
    void drawOval_renderingDisabled_doesntRender() {
        standbyProxyRenderer.drawOval(Offset.at(10, 2), 10, 40, OvalDrawOptions.outline(Color.RED), CLIP);

        verifyNoInteractions(renderer);
    }

    @Test
    void drawOval_renderingEnabled_renders() {
        standbyProxyRenderer.toggleOnOff();

        standbyProxyRenderer.drawOval(Offset.at(10, 2), 10, 40, OvalDrawOptions.outline(Color.RED), CLIP);

        verify(renderer).drawOval(Offset.at(10, 2), 10, 40, OvalDrawOptions.outline(Color.RED), CLIP);
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
