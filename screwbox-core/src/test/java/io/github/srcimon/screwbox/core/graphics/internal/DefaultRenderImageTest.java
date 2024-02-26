package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static io.github.srcimon.screwbox.core.Percent.half;
import static io.github.srcimon.screwbox.core.Rotation.degrees;
import static io.github.srcimon.screwbox.core.graphics.Color.*;
import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.filled;
import static org.assertj.core.api.Assertions.assertThat;

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
    }

    @Test
    void fillWith_colorRed_fillsWholeImageWithRed() {
        renderer.fillWith(RED);

        assertThat(result.colorAt(0, 0)).isEqualTo(RED);
        assertThat(result.colorAt(20, 30)).isEqualTo(RED);
    }

    @Test
    void drawRectangle_colorBlue_fillsRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(4, 4), filled(BLUE));

        assertThat(result.colorAt(0, 0)).isEqualTo(BLACK);
        assertThat(result.colorAt(10, 10)).isEqualTo(BLUE);
        assertThat(result.colorAt(12, 12)).isEqualTo(BLUE);
        assertThat(result.colorAt(20, 20)).isEqualTo(BLACK);
    }

    @Test
    void drawRectangle_rotatedRectangle_fillsRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(20, 40), filled(BLUE).rotation(degrees(45)));

        assertThat(result.colorAt(14, 14)).isEqualTo(BLACK);
        assertThat(result.colorAt(14, 28)).isEqualTo(BLUE);
        assertThat(result.colorAt(38, 17)).isEqualTo(BLACK);
        assertThat(result.colorAt(38, 23)).isEqualTo(BLUE);
    }

    @Test
    void drawRectangle_colorBlueWithOpacityHalf_fillsRectangleBlueAndAppliesOpacityChanges() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(4, 4), filled(BLUE.opacity(half())));

        assertThat(result.colorAt(0, 0)).isEqualTo(BLACK);
        assertThat(result.colorAt(10, 10)).isEqualTo(Color.rgb(0, 0, 127));
        assertThat(result.colorAt(20, 20)).isEqualTo(BLACK);
    }

    @Test
    void drawRectangle_outline_onlyPaintsOutline() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(10, 10), RectangleDrawOptions.outline(BLUE).strokeWidth(2));

        assertThat(result.colorAt(0, 0)).isEqualTo(BLACK);
        assertThat(result.colorAt(10, 10)).isEqualTo(BLUE);
        assertThat(result.colorAt(15, 15)).isEqualTo(BLACK);
        assertThat(result.colorAt(20, 20)).isEqualTo(BLUE);
    }
}
