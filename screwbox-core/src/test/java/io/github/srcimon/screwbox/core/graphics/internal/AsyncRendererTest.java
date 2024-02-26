package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

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
    void applyDrawActions_noUpdate_nextRendererNotInvoked() {
        asyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), Color.YELLOW);

        verify(renderer, never()).drawLine(Offset.origin(), Offset.at(10, 20), Color.YELLOW);
        verify(renderer, never()).updateGraphicsContext(any());
    }

    @Test
    void applyDrawActions_update_nextRendererInvoked() {
        asyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), Color.YELLOW);
        asyncRenderer.fillCircle(Offset.origin(), 25, Color.BLUE);

        asyncRenderer.updateGraphicsContext(null);

        verify(renderer, timeout(1000)).drawLine(Offset.origin(), Offset.at(10, 20), Color.YELLOW);
        verify(renderer, timeout(1000)).fillCircle(Offset.origin(), 25, Color.BLUE);
        verify(renderer, timeout(1000)).updateGraphicsContext(null);
    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }
}
