package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExtendWith(MockitoExtension.class)
class RenderingIntegrationTest {

    Renderer renderer;
    ExecutorService executor;
    AsyncRenderer asyncRenderer;

    @Mock
    WindowFrame frame;

    @Mock
    Robot robot;

    Graphics2D graphics;

    Image image;

    @BeforeEach
    void beforeEach() {
        Mockito.when(frame.getWidth()).thenReturn(320);
        Mockito.when(frame.getHeight()).thenReturn(240);
        BufferStrategy bufferStrategy = Mockito.mock(BufferStrategy.class);
        Canvas canvas = Mockito.mock(Canvas.class);
        Mockito.when(canvas.getBufferStrategy()).thenReturn(bufferStrategy);
        Mockito.when(frame.getCanvas()).thenReturn(canvas);

        executor = Executors.newSingleThreadExecutor();
        image = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB);
        graphics = (Graphics2D) image.getGraphics();
        renderer = new DefaultRenderer(frame, graphics, robot);
        asyncRenderer = new AsyncRenderer(renderer, executor);
        Mockito.when(bufferStrategy.getDrawGraphics()).thenReturn(graphics);

    }

    @Test
    void xxx() throws IOException {
        renderer.fillWith(Color.RED);
        renderer.drawCircle(Offset.at(4, 10), 4, Color.BLUE, 3);
        renderer.updateScreen(true);

        var sprite = Sprite.fromImage(image);
        Assertions.assertThat(sprite.frame(0).colorAt(0, 0)).isEqualTo(Color.RED);
        ImageIO.write(ImageUtil.toBufferedImage(image), "png", new File("save.png"));

    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }
}
