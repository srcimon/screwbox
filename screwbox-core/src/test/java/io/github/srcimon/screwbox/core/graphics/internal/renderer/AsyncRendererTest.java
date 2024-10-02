package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.graphics.Color.YELLOW;
import static io.github.srcimon.screwbox.core.graphics.LineDrawOptions.color;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AsyncRendererTest {

    @Mock
    Renderer renderer;

    ExecutorService executor;
    AsyncRenderer asyncRenderer;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        asyncRenderer = new AsyncRenderer(renderer, executor);
    }

    @Test
    void renderDuration_renderingDone_hasDuration() {
        asyncRenderer.updateGraphicsContext(null, Size.of(10, 10));
        asyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW));
        asyncRenderer.updateGraphicsContext(null, Size.of(10, 10));

        assertThat(asyncRenderer.renderDuration().nanos()).isNotZero();
    }

    @Test
    void applyDrawActions_noUpdate_nextRendererNotInvoked() {
        asyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW));

        verify(renderer, never()).drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW));
        verify(renderer, never()).updateGraphicsContext(any(), any());
    }

    @Test
    void applyDrawActions_update_nextRendererInvoked() {
        asyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW));
        asyncRenderer.drawCircle(Offset.origin(), 25, CircleDrawOptions.filled(Color.BLUE));

        asyncRenderer.updateGraphicsContext(null, Size.of(10, 10));

        verify(renderer, timeout(1000)).drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW));
        verify(renderer, timeout(1000)).drawCircle(Offset.origin(), 25, CircleDrawOptions.filled(Color.BLUE));
        verify(renderer, timeout(1000)).updateGraphicsContext(null, Size.of(10, 10));
    }

    @Test
    void fillWith_afterUpdateOfGraphicsContext_callsNextRenderer() {
        asyncRenderer.fillWith(Color.BLUE);

        asyncRenderer.updateGraphicsContext(null, Size.of(10, 10));
        verify(renderer, timeout(1000)).fillWith(Color.BLUE);
    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }
}
