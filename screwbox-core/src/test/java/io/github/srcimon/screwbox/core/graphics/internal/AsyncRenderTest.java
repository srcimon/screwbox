package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleOptions;
import io.github.srcimon.screwbox.core.graphics.Size;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.test.TestUtil.shutdown;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AsyncRenderTest {

    AsyncRenderer renderer;

    ExecutorService executor;

    @Mock
    Renderer next;

    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadExecutor();
        renderer = new AsyncRenderer(next, executor);
    }

    @Test
    void drawRectangle_screenNotUpdated_doesntCallNextRender() {
        renderer.drawRectangle(Offset.at(4, 2), Size.of(9, 3), RectangleOptions.filled(RED));

        verify(next, never()).drawRectangle(any(), any(), any());
    }

    @Test
    void drawRectangle_screenUpdated_callsNextRender() {
        renderer.drawRectangle(Offset.at(4, 2), Size.of(9, 3), RectangleOptions.filled(RED));

        renderer.updateScreen(true);

        verify(next).drawRectangle(Offset.at(4, 2), Size.of(9, 3), RectangleOptions.filled(RED));
    }


    @AfterEach
    void afterEach() {
        shutdown(executor);
    }
}
