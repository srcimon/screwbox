package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.screwbox.core.test.TestUtil.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class RendererPipelineTest {

    RenderPipeline renderPipeline;
    ExecutorService executorService;
    Image image;

    @BeforeEach
    void setUp() {
        final var engine = Mockito.mock(Engine.class);
        when(engine.environment()).thenReturn(new DefaultEnvironment(engine));
        executorService = Executors.newSingleThreadExecutor();
        renderPipeline = new RenderPipeline(executorService, new GraphicsConfiguration(), engine);
        image = ImageOperations.createImage(Size.of(240, 160));
        renderPipeline.toggleOnOff();
        updateContext();
    }

    @Test
    void renderDuration_renderingHappened_hasDuration() {
        renderPipeline.renderer().fillWith(SpriteBundle.EXPLOSION.get(), SpriteFillOptions.scale(2), new ScreenBounds(Size.of(240, 160)));
        updateContext();

        assertThat(renderPipeline.renderDuration().nanos()).isPositive();
    }

    @Test
    void renderTaskCount_renderingHappened_hasTaskCount() {
        renderPipeline.renderer().fillWith(SpriteBundle.EXPLOSION.get(), SpriteFillOptions.scale(2), new ScreenBounds(Size.of(240, 160)));
        renderPipeline.renderer().fillWith(SpriteBundle.EXPLOSION.get(), SpriteFillOptions.scale(2), new ScreenBounds(Size.of(240, 160)));
        updateContext();

        await(() -> renderPipeline.renderTaskCount() == 2, Duration.ofMillis(200));
        assertThat(renderPipeline.renderTaskCount()).isEqualTo(2);
    }

    void updateContext() {
        renderPipeline.renderer().updateContext(() -> (Graphics2D) image.getGraphics());
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executorService);
    }
}
