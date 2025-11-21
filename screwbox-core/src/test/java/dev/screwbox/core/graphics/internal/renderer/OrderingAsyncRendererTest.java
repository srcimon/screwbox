package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.Renderer;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.screwbox.core.graphics.Color.YELLOW;
import static dev.screwbox.core.graphics.options.LineDrawOptions.color;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings
class OrderingAsyncRendererTest {

    private static final ScreenBounds CLIP = new ScreenBounds(Size.of(10, 10));

    @Mock
    Environment environment;

    @Mock
    Renderer renderer;

    ExecutorService executor;

    OrderingAsyncRenderer orderingAsyncRenderer;

    @BeforeEach
    void beforeEach() {
        Engine engine = mock(Engine.class);
        when(engine.environment()).thenReturn(environment);
        executor = Executors.newSingleThreadExecutor();
        orderingAsyncRenderer = new OrderingAsyncRenderer(renderer, executor, engine);
    }

    @Test
    void renderDuration_renderingDone_hasDuration() {
        orderingAsyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
        executeAsyncTasks();

        assertThat(orderingAsyncRenderer.renderDuration().nanos()).isNotZero();
    }

    @Test
    void renderTaskCount_twoTasksExecuted_isTwo() {
        orderingAsyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
        orderingAsyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
        executeAsyncTasks();

        TestUtil.await(() -> orderingAsyncRenderer.renderTaskCount() == 2, Duration.ofMillis(100));
    }

    @Test
    void applyDrawActions_noUpdate_nextRendererNotInvoked() {
        orderingAsyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);

        verify(renderer, never()).drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
        verify(renderer, never()).updateContext(any());
    }

    @Test
    void applyDrawActions_update_nextRendererInvoked() {
        orderingAsyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
        orderingAsyncRenderer.drawOval(Offset.origin(), 25, 25, OvalDrawOptions.filled(Color.BLUE), CLIP);

        orderingAsyncRenderer.updateContext(null);

        verify(renderer, timeout(1000)).drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
        verify(renderer, timeout(1000)).drawOval(Offset.origin(), 25, 25, OvalDrawOptions.filled(Color.BLUE), CLIP);
        verify(renderer, timeout(1000)).updateContext(null);
    }

    @Test
    void applyDrawActions_unsorted_executesInOrder() {
        when(environment.currentDrawOrder()).thenReturn(3_000_000);
        Sprite sprite = SpriteBundle.FIRE.get();
        orderingAsyncRenderer.drawSprite(sprite, Offset.at(0, 0), SpriteDrawOptions.scaled(1).drawOrder(1).zIndex(2), CLIP);
        orderingAsyncRenderer.drawText(Offset.at(2, 0), "Text", SystemTextDrawOptions.systemFont("Arial").drawOrder(2), CLIP);
        orderingAsyncRenderer.drawSprite(sprite, Offset.at(1, 0), SpriteDrawOptions.scaled(1).drawOrder(1).zIndex(1), CLIP);
        orderingAsyncRenderer.drawSprite(sprite, Offset.at(3, 0), SpriteDrawOptions.scaled(1).drawOrder(2_000_000).zIndex(1), CLIP);

        executeAsyncTasks();

        InOrder inOrder = inOrder(renderer);
        inOrder.verify(renderer, timeout(1000)).drawSprite(sprite, Offset.at(3, 0), SpriteDrawOptions.scaled(1).drawOrder(2_000_000).zIndex(1), CLIP);
        inOrder.verify(renderer, timeout(1000)).drawSprite(sprite, Offset.at(1, 0), SpriteDrawOptions.scaled(1).drawOrder(1).zIndex(1), CLIP);
        inOrder.verify(renderer, timeout(1000)).drawSprite(sprite, Offset.at(0, 0), SpriteDrawOptions.scaled(1).drawOrder(1).zIndex(2), CLIP);
        inOrder.verify(renderer, timeout(1000)).drawText(Offset.at(2, 0), "Text", SystemTextDrawOptions.systemFont("Arial").drawOrder(2), CLIP);
    }

    @Test
    void fillWith_afterUpdateOfGraphicsContext_callsNextRenderer() {
        orderingAsyncRenderer.fillWith(Color.BLUE, CLIP);

        orderingAsyncRenderer.updateContext(null);
        verify(renderer, timeout(1000)).fillWith(Color.BLUE, CLIP);
    }


    private void executeAsyncTasks() {
        orderingAsyncRenderer.updateContext(null);
        orderingAsyncRenderer.updateContext(null);
    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }
}
