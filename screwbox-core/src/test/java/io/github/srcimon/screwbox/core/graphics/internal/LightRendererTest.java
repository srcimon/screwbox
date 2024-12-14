package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

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
    GraphicsConfiguration configuration;
    LightRenderer lightRenderer;

    @BeforeEach
    void setUp() {
        canvas = new DefaultCanvas(renderer, new ScreenBounds(0, 0, 160, 80));
        viewport = new DefaultViewport(canvas, new DefaultCamera(canvas));
        configuration = new GraphicsConfiguration();
        executor = Executors.newSingleThreadExecutor();
        lightRenderer = new LightRenderer(lightPhysics, configuration, executor, viewport, postFilter -> postFilter);
    }

    @Test
    void renderLight_noLights_isBlack() {
        var sprite = lightRenderer.renderLight();

        assertThat(sprite.get().singleFrame().colors()).containsExactly(Color.BLACK);
    }

    @Test
    void renderLight_spotLightPresent_createsImage() {
        lightRenderer.addSpotLight(Vector.$(60, 20), 40, Color.BLACK);
        var sprite = lightRenderer.renderLight();

        verifyIsIdenticalWithReferenceImage(sprite, "renderLight_spotLightPresent_createsImage.png");
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }

    private void verifyIsIdenticalWithReferenceImage(Asset<Sprite> sprite, String fileName) {
        Frame reference = Frame.fromFile("lightrenderer/" + fileName);
        assertThat(sprite.get().singleFrame().listPixelDifferences(reference)).isEmpty();
    }
}
