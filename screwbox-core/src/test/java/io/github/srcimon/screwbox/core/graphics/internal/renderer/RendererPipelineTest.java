package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class RendererPipelineTest {

    RenderPipeline renderPipeline;
    ExecutorService executorService;

    @BeforeEach
    void setUp() {
        executorService = Executors.newSingleThreadExecutor();
        renderPipeline = new RenderPipeline(executorService);
    }

    @Test
    void renderDuration_notRenderedYet_isNone() {
        assertThat(renderPipeline.renderDuration()).isEqualTo(Duration.none());
    }

    //TODO add real tests

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executorService);
    }
}
