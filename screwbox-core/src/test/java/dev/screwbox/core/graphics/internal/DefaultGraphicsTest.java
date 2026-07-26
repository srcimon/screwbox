package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultGraphicsTest {

    @InjectMocks
    DefaultGraphics graphics;

    @Mock
    RenderPipeline renderPipeline;

    @Spy
    GraphicsConfiguration configuration = new GraphicsConfiguration(RenderingApi.DIRECT_3D);

    @Test
    void renderDuration_returnsRenderDurationFromPipeline() {
        when(renderPipeline.renderDuration()).thenReturn(Duration.ofMicros(20));
        assertThat(graphics.renderDuration()).isEqualTo(Duration.ofMicros(20));
    }

    @Test
    void renderTaskCount_returnsRenderTaskCountFromPipeline() {
        when(renderPipeline.renderTaskCount()).thenReturn(413);
        assertThat(graphics.renderTaskCount()).isEqualTo(413);
    }
}
