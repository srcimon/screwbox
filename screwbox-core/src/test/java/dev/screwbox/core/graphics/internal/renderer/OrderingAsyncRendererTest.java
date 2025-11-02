package dev.screwbox.core.graphics.internal.renderer;

import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@MockitoSettings
class OrderingAsyncRendererTest {

//    private static final ScreenBounds CLIP = new ScreenBounds(Offset.origin(), Size.of(10, 10));
//    @Mock
//    Renderer renderer;
//
//    ExecutorService executor;
//    AsyncRenderer asyncRenderer;
//
//    @BeforeEach
//    void beforeEach() {
//        executor = Executors.newSingleThreadExecutor();
//        asyncRenderer = new AsyncRenderer(renderer, executor);
//    }
//
//    @Test
//    void renderDuration_renderingDone_hasDuration() {
//        asyncRenderer.updateContext(null);
//        asyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
//        asyncRenderer.updateContext(null);
//
//        assertThat(asyncRenderer.renderDuration().nanos()).isNotZero();
//    }
//
//    @Test
//    void applyDrawActions_noUpdate_nextRendererNotInvoked() {
//        asyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
//
//        verify(renderer, never()).drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
//        verify(renderer, never()).updateContext(any());
//    }
//
//    @Test
//    void applyDrawActions_update_nextRendererInvoked() {
//        asyncRenderer.drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
//        asyncRenderer.drawOval(Offset.origin(), 25, 25, OvalDrawOptions.filled(Color.BLUE), CLIP);
//
//        asyncRenderer.updateContext(null);
//
//        verify(renderer, timeout(1000)).drawLine(Offset.origin(), Offset.at(10, 20), color(YELLOW), CLIP);
//        verify(renderer, timeout(1000)).drawOval(Offset.origin(), 25, 25, OvalDrawOptions.filled(Color.BLUE), CLIP);
//        verify(renderer, timeout(1000)).updateContext(null);
//    }
//
//    @Test
//    void fillWith_afterUpdateOfGraphicsContext_callsNextRenderer() {
//        asyncRenderer.fillWith(Color.BLUE, CLIP);
//
//        asyncRenderer.updateContext(null);
//        verify(renderer, timeout(1000)).fillWith(Color.BLUE, CLIP);
//    }
//
//    @AfterEach
//    void afterEach() {
//        executor.shutdown();
//    }
}
