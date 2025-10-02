package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.LensFlareBundle;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@Timeout(1)
@MockitoSettings
class LightRendererTest {

    @Mock
    Renderer renderer;

    @Mock
    LightPhysics lightPhysics;

    ExecutorService executor;
    DefaultCanvas canvas;
    Viewport viewport;
    LightRenderer lightRenderer;

    @BeforeEach
    void setUp() {
        canvas = new DefaultCanvas(renderer, new ScreenBounds(0, 0, 160, 80));
        viewport = new DefaultViewport(canvas, new DefaultCamera(canvas));
        executor = Executors.newSingleThreadExecutor();
        final var lightmap = new Lightmap(viewport.canvas().size(), 4, Percent.max());
        lightRenderer = new LightRenderer(lightPhysics, executor, viewport,  true, lightmap, postFilter -> postFilter);
    }

    @Test
    void renderLight_noLights_isBlack() {
        var sprite = lightRenderer.renderLight();

        assertCompletelyBlack(sprite);
    }

    @Test
    void renderLight_lightIsObscuredByShadowCaster_isBlack() {
        lightRenderer.addPointLight($(60, 20), 40, Color.BLACK);
        lightPhysics.addShadowCaster($$(10, 10, 400, 400));

        var sprite = lightRenderer.renderLight();

        assertCompletelyBlack(sprite);
    }

    @Test
    void renderLight_lightBlockedByShadowCasterButHasOrthographicWallOnTop_isVisible() {
        lightRenderer.addSpotLight($(60, 40), 40, Color.BLACK);
        lightRenderer.addOrthographicWall($$(20, 20, 20, 20));
        lightPhysics.addShadowCaster($$(20, 20, 20, 20));

        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_lightBlockedByShadowCasterButHasOrthographicWallOnTop_isVisible.png");
    }

    @Test
    void renderLight_spotLightPresent_createsImage() {
        lightRenderer.addSpotLight($(60, 20), 40, Color.BLACK);
        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_spotLightPresent_createsImage.png");
    }

    @Test
    void renderLight_expandedLightsPresent_createsImage() {
        lightRenderer.addExpandedLight($$(15, 10, 30, 30), Color.BLACK.opacity(Percent.half()), 0, false);
        lightRenderer.addExpandedLight($$(10, 20, 30, 30), Color.BLACK, 0, false);

        var sprite = lightRenderer.renderLight();
        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_expandedLightsPresent_createsImage.png");
    }

    @Test
    void renderLight_expandedLightFadingOut_createsImage() {
        lightRenderer.addExpandedLight($$(2, 2, 50, 30), Color.BLACK, 20, true);

        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_expandedLightFadingOut_createsImage.png");
    }

    @Test
    void renderGlows_glowPresent_rendersGlowAndLensFlare() {
        lightRenderer.addGlow($(8, 8), 4, Color.WHITE.opacity(0.5), LensFlareBundle.SHY.get());

        lightRenderer.renderGlows();

        verify(renderer, times(3)).drawCircle(any(), anyInt(), any(), any());
    }

    @Test
    void renderGlows_expandedGlowPresent_rendersGlowAndLensFlare() {
        lightRenderer.addGlow($$(4, 4, 209, 40), 20, Color.BLUE, LensFlareBundle.SHY.get());

        lightRenderer.renderGlows();

        verify(renderer, times(3)).drawRectangle(any(), any(), any(), any());
    }

    @Test
    void renderGlows_expandedGlowOutOfVisibleArea_doesntRenderAnything() {
        lightRenderer.addGlow($$(4, 400, 209, 40), 20, Color.BLUE, LensFlareBundle.SHY.get());

        lightRenderer.renderGlows();

        verifyNoInteractions(renderer);
    }

    @Test
    void renderGlows_glowPresentLensFlareDisabled_rendersGlowOnly() {
        final var lightmap = new Lightmap(viewport.canvas().size(), 4, Percent.max());
        lightRenderer = new LightRenderer(lightPhysics, executor, viewport, false, lightmap, postFilter -> postFilter);
        lightRenderer.addGlow($(8, 8), 4, Color.WHITE.opacity(0.5), LensFlareBundle.SHY.get());

        lightRenderer.renderGlows();

        verify(renderer).drawCircle(any(), anyInt(), any(), any());
    }

    @Test
    void renderGlows_glowPresentOutsideOfFrame_doesntRenderGlowAndLensFlares() {
        lightRenderer.addGlow($(80, 80), 4, Color.WHITE.opacity(0.5), LensFlareBundle.SHY.get());

        lightRenderer.renderGlows();

        verifyNoInteractions(renderer);
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }

    private void verifyIsIdenticalWithReferenceImage(Asset<Sprite> sprite, String fileName) {
        Frame reference = Frame.fromFile("lightrenderer/" + fileName);
        assertThat(sprite.get().singleFrame().hasIdenticalPixels(reference)).isTrue();
    }

    private void assertCompletelyBlack(Asset<Sprite> sprite) {
        assertThat(sprite.get().singleFrame().colors()).containsExactly(Color.BLACK);
    }
}
