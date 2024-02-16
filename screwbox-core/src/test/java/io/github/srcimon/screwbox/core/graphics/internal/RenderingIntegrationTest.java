package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RenderingIntegrationTest {

    Renderer renderer;
    ExecutorService executor;
    AsyncRenderer asyncRenderer;

    @Mock
    WindowFrame frame;

    @Mock
    Robot robot;

    Frame result;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        Image image = new BufferedImage(80, 40, BufferedImage.TYPE_INT_ARGB);
        result = Frame.fromImage(image);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer = new DefaultRenderer(frame, graphics, robot);
        asyncRenderer = new AsyncRenderer(renderer, executor);

        BufferStrategy bufferStrategy = Mockito.mock(BufferStrategy.class);
        Canvas canvas = Mockito.mock(Canvas.class);
        when(frame.getCanvas()).thenReturn(canvas);
        when(canvas.getBufferStrategy()).thenReturn(bufferStrategy);
        when(bufferStrategy.getDrawGraphics()).thenReturn(graphics);
    }

    @Test
    void fillWith_colorRed_fillsWholeImageWithRed() {
        when(frame.getWidth()).thenReturn(result.size().width());
        when(frame.getHeight()).thenReturn(result.size().height());

        renderer.fillWith(Color.RED);
        renderer.drawCircle(Offset.at(4, 10), 4, Color.BLUE, 3);
        renderer.updateScreen(true);

        assertThat(result.colorAt(0, 0)).isEqualTo(Color.RED);
        assertThat(result.colorAt(20, 30)).isEqualTo(Color.RED);
    }

    @Test
    void fillRectangle_colorBlue_fillsRectangleBlue() {
        renderer.fillRectangle(new ScreenBounds(10, 10, 4, 4), Color.BLUE);
        renderer.updateScreen(true);

        assertThat(result.colorAt(0, 0)).isEqualTo(Color.TRANSPARENT);
        assertThat(result.colorAt(10, 10)).isEqualTo(Color.BLUE);
        assertThat(result.colorAt(12, 12)).isEqualTo(Color.BLUE);
        assertThat(result.colorAt(20, 20)).isEqualTo(Color.TRANSPARENT);
    }

    @Test
    void fillRectangle_colorBlueWithOpacityHalf_fillsRectangleBlueAndAppliesOpacityChanges() {
        renderer.fillRectangle(new ScreenBounds(10, 10, 4, 4), Color.BLUE.opacity(Percent.half()));
        renderer.updateScreen(true);

        assertThat(result.colorAt(0, 0)).isEqualTo(Color.TRANSPARENT);
        assertThat(result.colorAt(10, 10).b()).isEqualTo(255);
        assertThat(result.colorAt(10, 10).opacity().value()).isEqualTo(0.5, offset(0.05));
        assertThat(result.colorAt(12, 12).b()).isEqualTo(255);
        assertThat(result.colorAt(12, 12).opacity().value()).isEqualTo(0.5, offset(0.05));
        assertThat(result.colorAt(20, 20)).isEqualTo(Color.TRANSPARENT);
    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }
}
