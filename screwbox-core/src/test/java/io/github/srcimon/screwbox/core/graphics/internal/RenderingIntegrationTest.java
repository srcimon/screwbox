package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
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
        Mockito.doAnswer(invocation -> ImageIO.write(ImageUtil.toBufferedImage(image), "png",new File("save.png"))).when(bufferStrategy).show();

    }

    @Test
    void xxx() throws IOException {
        renderer.fillWith(Color.RED);
        renderer.updateScreen(true);

    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }
}
