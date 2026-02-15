package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.LensFlareBundle;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.options.ShadowOptions;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.Spy;
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

    @Spy
    LightPhysics lightPhysics = new LightPhysics();

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
        lightRenderer = new LightRenderer(lightPhysics, executor, viewport, true, lightmap, postFilter -> postFilter);
    }

    @Test
    void renderLight_noLights_isBlack() {
        var sprite = lightRenderer.renderLight();

        assertCompletelyBlack(sprite);
    }

    @Test
    void renderLight_lightIsBlockedByOccluder_isBlack() {
        lightRenderer.addPointLight($(4, 4), 40, Color.BLACK);
        lightPhysics.addAffectedByShadowOccluder($$(0, 0, 10, 10));

        var sprite = lightRenderer.renderLight();
        assertCompletelyBlack(sprite);
    }

    @Test
    void renderLight_lightIsBlockedByNoSelfOccluder_isBlack() {
        lightRenderer.addPointLight($(4, 4), 40, Color.BLACK);
        lightPhysics.addOccluder($$(0, 0, 10, 10));

        var sprite = lightRenderer.renderLight();
        assertCompletelyBlack(sprite);
    }

    @Test
    void renderLight_multipleDirectionalLightsAndOccluders_rendersLight() {
        lightRenderer.addDirectionalLight(Line.between($(-40, -10), $(40, -50)), 80, Color.BLACK);
        lightRenderer.addDirectionalLight(Line.between($(-40, 50), $(-45, -50)), 180, Color.BLACK.opacity(0.5));
        lightPhysics.addAffectedByShadowOccluder($$(0, 0, 10, 10));
        lightPhysics.addOccluder($$(20, 0, 40, 20));

        var sprite = lightRenderer.renderLight();
        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_multipleDirectionalLightsAndOccluders_rendersLight.png");
    }

    @Test
    void renderLight_occluderPresent_lightStopsAtOccluder() {
        lightRenderer.addPointLight($(4, 4), 40, Color.BLACK);
        lightPhysics.addAffectedByShadowOccluder($$(10, 10, 400, 400));

        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_occluderPresent_lightStopsAtOccluder.png");
    }

    @Test
    void renderLight_lightBlockedByNoSelfOccluder_isVisible() {
        lightRenderer.addPointLight($(60, 40), 80, Color.BLACK);
        lightPhysics.addOccluder($$(20, 10, 20, 20));

        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_lightBlockedByNoSelfOccluder_isVisible.png");
    }

    @Test
    void renderLight_lightBlockedByOccluderButHasOrthographicWallOnTop_isVisible() {
        lightRenderer.addSpotLight($(60, 40), 40, Color.BLACK);
        lightRenderer.addOrthographicWall($$(20, 20, 20, 20));
        lightPhysics.addAffectedByShadowOccluder($$(20, 20, 20, 20));

        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_lightBlockedByOccluderButHasOrthographicWallOnTop_isVisible.png");
    }

    @Test
    void renderLight_spotLightPresent_createsImage() {
        lightRenderer.addSpotLight($(60, 20), 40, Color.BLACK);

        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_spotLightPresent_createsImage.png");
    }

    @Test
    void renderLight_coneLightPresent_createsImage() {
        lightRenderer.addConeLight($(40, 20), Angle.degrees(20), Angle.degrees(120), 60, Color.BLACK);
        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_coneLightPresent_createsImage.png");
    }

    @Test
    void renderLight_areaLightsPresent_createsImage() {
        lightRenderer.addAreaLight($$(15, 10, 30, 30), Color.BLACK.opacity(Percent.half()), 0, false);
        lightRenderer.addAreaLight($$(10, 20, 30, 30), Color.BLACK, 0, false);

        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_areaLightsPresent_createsImage.png");
    }

    @Test
    void renderLight_areaLightFadingOut_createsImage() {
        lightRenderer.addAreaLight($$(2, 2, 50, 30), Color.BLACK, 20, true);

        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_areaLightFadingOut_createsImage.png");
    }

    @Test
    void renderLight_backdropOccluderPresent_createsImage() {
        lightRenderer.addBackgdropOccluder(Polygon.fromBounds($$(4, 4, 20, 10)), ShadowOptions.rounded());
        lightRenderer.addPointLight($(4, 2), 80, Color.BLACK);
        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_backdropOccluderPresent_createsImage.png");
    }

    @Test
    void renderLight_angularAffectedBackdropOccluderPresent_createsImage() {
        lightRenderer.addBackgdropOccluder(Polygon.fromBounds($$(4, 4, 20, 10)), ShadowOptions.angular().affectOccluder());
        lightRenderer.addPointLight($(4, 2), 80, Color.BLACK);
        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_angularAffectedBackdropOccluderPresent_createsImage.png");
    }

    @Test
    void renderLight_backdropOccluderAndAreaLightPresent_createsImage() {
        lightRenderer.addBackgdropOccluder(Polygon.fromBounds($$(30, 20, 30, 20)), ShadowOptions.rounded());
        lightRenderer.addAreaLight($$(4, 4, 80, 50), Color.BLACK, 20, true);
        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_backdropOccluderAndAreaLightPresent_createsImage.png");
    }

    @Test
    void renderGlows_glowPresent_rendersGlowAndLensFlare() {
        lightRenderer.addGlow($(8, 8), 4, Color.WHITE.opacity(0.5), LensFlareBundle.SHY.get());

        lightRenderer.renderGlows();

        verify(renderer, times(3)).drawOval(any(), anyInt(), anyInt(), any(), any());
    }

    @Test
    void renderGlows_coneGlowPresent_rendersGlowAndLensFlare() {
        lightRenderer.addConeGlow($(8, 8), Angle.degrees(40), Angle.degrees(120), 4, Color.WHITE.opacity(0.5));

        lightRenderer.renderGlows();

        verify(renderer).drawOval(any(), anyInt(), anyInt(), any(), any());
    }

    @Test
    void renderGlows_areaGlowPresent_rendersGlowAndLensFlare() {
        lightRenderer.addGlow($$(4, 4, 209, 40), 20, Color.BLUE, LensFlareBundle.SHY.get());

        lightRenderer.renderGlows();

        verify(renderer, times(3)).drawRectangle(any(), any(), any(), any());
    }

    @Test
    void renderGlows_areaGlowOutOfVisibleArea_doesntRenderAnything() {
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

        verify(renderer).drawOval(any(), anyInt(), anyInt(), any(), any());
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
