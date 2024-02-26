package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.Percent.half;
import static io.github.srcimon.screwbox.core.graphics.Color.BLUE;
import static io.github.srcimon.screwbox.core.graphics.Color.TRANSPARENT;
import static io.github.srcimon.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultRenderImageTest {

    Frame result;
    Renderer renderer;

    @BeforeEach
    void beforeEach() {

        Image image = new BufferedImage(80, 40, BufferedImage.TYPE_INT_ARGB);
        result = Frame.fromImage(image);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        renderer = new DefaultRenderer();
        renderer.updateGraphicsContext(() -> graphics, Size.of(80, 40));

//        renderer = new DefaultRenderer(frame, graphics);
//
//        BufferStrategy bufferStrategy = Mockito.mock(BufferStrategy.class);
//        Canvas canvas = Mockito.mock(Canvas.class);
//        when(frame.getCanvas()).thenReturn(canvas);
//        when(canvas.getBufferStrategy()).thenReturn(bufferStrategy);
//        when(bufferStrategy.getDrawGraphics()).thenReturn(graphics);
    }

    @Test
    void fillWith_colorRed_fillsWholeImageWithRed() {

        renderer.fillWith(Color.RED);


        assertThat(result.colorAt(0, 0)).isEqualTo(Color.RED);
        assertThat(result.colorAt(20, 30)).isEqualTo(Color.RED);
    }

    @Test
    void drawRectangle_colorBlue_fillsRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(4, 4), RectangleDrawOptions.filled(BLUE));

        assertThat(result.colorAt(0, 0)).isEqualTo(TRANSPARENT);
        assertThat(result.colorAt(10, 10)).isEqualTo(BLUE);
        assertThat(result.colorAt(12, 12)).isEqualTo(BLUE);
        assertThat(result.colorAt(20, 20)).isEqualTo(TRANSPARENT);
    }

    @Test
    void drawRectangle_colorBlueWithOpacityHalf_fillsRectangleBlueAndAppliesOpacityChanges() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(4, 4), RectangleDrawOptions.filled(BLUE.opacity(half())));

        assertThat(result.colorAt(0, 0)).isEqualTo(TRANSPARENT);
        assertThat(result.colorAt(10, 10).b()).isEqualTo(255);
        assertThat(result.colorAt(10, 10).opacity().value()).isEqualTo(0.5, offset(0.05));
        assertThat(result.colorAt(12, 12).b()).isEqualTo(255);
        assertThat(result.colorAt(12, 12).opacity().value()).isEqualTo(0.5, offset(0.05));
        assertThat(result.colorAt(20, 20)).isEqualTo(TRANSPARENT);
    }

    @Test
    void drawRectangle_outline_onlyPaintsOutline() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(10, 10), RectangleDrawOptions.outline(BLUE).strokeWidth(2));

        assertThat(result.colorAt(0, 0)).isEqualTo(TRANSPARENT);
        assertThat(result.colorAt(10, 10)).isEqualTo(BLUE);
        assertThat(result.colorAt(15, 15)).isEqualTo(TRANSPARENT);
        assertThat(result.colorAt(20, 20)).isEqualTo(BLUE);
    }
}
