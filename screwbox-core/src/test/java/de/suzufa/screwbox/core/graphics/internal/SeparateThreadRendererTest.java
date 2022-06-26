package de.suzufa.screwbox.core.graphics.internal;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;

@ExtendWith(MockitoExtension.class)
class SeparateThreadRendererTest {

    @Mock
    Renderer renderer;

    ExecutorService executor;
    SeparateThreadRenderer separateThreadRenderer;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        separateThreadRenderer = new SeparateThreadRenderer(renderer, executor);
    }

    @Test
    void applyDrawActions_noUpdate_nextRendererNotInvoked() {
        separateThreadRenderer.drawLine(Offset.origin(), Offset.at(10, 20), Color.YELLOW);

        verify(renderer, never()).drawLine(Offset.origin(), Offset.at(10, 20), Color.YELLOW);
        verify(renderer, never()).updateScreen(anyBoolean());
    }

    @Test
    void applyDrawActions_update_nextRendererInvoked() {
        separateThreadRenderer.drawPolygon(emptyList(), Color.BLACK);
        separateThreadRenderer.drawCircle(Offset.origin(), 25, Color.BLUE);

        separateThreadRenderer.updateScreen(true);

        verify(renderer, timeout(1000)).drawPolygon(emptyList(), Color.BLACK);
        verify(renderer, timeout(1000)).drawCircle(Offset.origin(), 25, Color.BLUE);
        verify(renderer, timeout(1000)).updateScreen(true);
    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }
}
