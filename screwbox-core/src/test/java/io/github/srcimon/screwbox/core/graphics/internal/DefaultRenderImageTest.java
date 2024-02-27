package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.github.srcimon.screwbox.core.Percent.half;
import static io.github.srcimon.screwbox.core.Rotation.degrees;
import static io.github.srcimon.screwbox.core.graphics.Color.BLUE;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.filled;
import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.outline;
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

        verifyIsIdenticalWithReferenceImage("fillWith_colorRed_fillsWholeImageWithRed.png");
    }

    @Test
    void drawRectangle_colorBlue_fillsRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(4, 4), filled(BLUE));

        verifyIsIdenticalWithReferenceImage("drawRectangle_colorBlue_fillsRectangleBlue.png");
    }

    @Test
    void drawRectangle_rotated_fillsRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(20, 40), filled(BLUE).rotation(degrees(45)));

        verifyIsIdenticalWithReferenceImage("drawRectangle_rotated_fillsRectangleBlue.png");
    }

    @Test
    void drawRectangle_rotatedOutline_outlineRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(20, 40), outline(BLUE).strokeWidth(3).rotation(degrees(45)));

        verifyIsIdenticalWithReferenceImage("drawRectangle_rotatedOutline_outlineRectangleBlue.png");
    }

    @Test
    void drawRectangle_halfOpacity_appliesOpacityChanges() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(4, 4), filled(BLUE.opacity(half())));

        verifyIsIdenticalWithReferenceImage("drawRectangle_halfOpacity_appliesOpacityChanges.png");
    }

    @Test
    void drawRectangle_outline_onlyPaintsOutline() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(10, 10), RectangleDrawOptions.outline(BLUE).strokeWidth(2));

        verifyIsIdenticalWithReferenceImage("drawRectangle_outline_onlyPaintsOutline.png");
    }

    private void verifyIsIdenticalWithReferenceImage(String fileName) {
        Frame reference = Frame.fromFile("renderer/" + fileName);
        assertThat(result.listPixelDifferences(reference)).isEmpty();
    }
}
