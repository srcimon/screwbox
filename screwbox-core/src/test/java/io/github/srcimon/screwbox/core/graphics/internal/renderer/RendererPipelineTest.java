package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class RendererPipelineTest {

    RenderPipeline renderPipeline;
    ExecutorService executorService;
    Image image;

    @BeforeEach
    void setUp() {
        executorService = Executors.newSingleThreadExecutor();
        renderPipeline = new RenderPipeline(executorService);
        image = new BufferedImage(240, 160, BufferedImage.TYPE_INT_ARGB);
        renderPipeline.toggleOnOff();
        updateContext();
    }

    @Test
    void renderDuration_renderingHappened_hasDuration() {
        renderPipeline.renderer().fillWith(SpriteBundle.EXPLOSION.get(), SpriteFillOptions.scale(2), new ScreenBounds(Size.of(240, 160)));
        updateContext();

        assertThat(renderPipeline.renderDuration().nanos()).isPositive();
    }

    void updateContext() {
        renderPipeline.renderer().updateContext(() -> (Graphics2D) image.getGraphics());
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executorService);
    }
}
