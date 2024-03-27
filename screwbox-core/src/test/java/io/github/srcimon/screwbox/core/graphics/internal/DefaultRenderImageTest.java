package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.github.srcimon.screwbox.core.Percent.half;
import static io.github.srcimon.screwbox.core.Rotation.degrees;
import static io.github.srcimon.screwbox.core.graphics.Color.BLUE;
import static io.github.srcimon.screwbox.core.graphics.Color.ORANGE;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.LineDrawOptions.color;
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

    @Test
    void drawLine_usingThickStroke_paintsUsingThickStroke() {
        renderer.drawLine(Offset.at(4, 5), Offset.at(20, 20), color(ORANGE).strokeWidth(2));

        verifyIsIdenticalWithReferenceImage("drawLine_usingThickStroke_paintsUsingThickStroke.png");
    }

    @Test
    void drawLine_noStrokeSet_paintsUsingThinStroke() {
        renderer.drawLine(Offset.at(8, 1), Offset.at(40, 30), color(ORANGE));

        verifyIsIdenticalWithReferenceImage("drawLine_noStrokeSet_paintsUsingThinStroke.png");
    }

    @Test
    void drawCircle_noStrokeSet_paintsUsingThinStroke() {
        renderer.drawCircle(Offset.at(10, 10), 4, CircleDrawOptions.outline(WHITE));

        verifyIsIdenticalWithReferenceImage("drawCircle_noStrokeSet_paintsUsingThinStroke.png");
    }

    @Test
    void drawCircle_thickStrokeSet_paintsUsingThickStroke() {
        renderer.drawCircle(Offset.at(20, 20), 8, CircleDrawOptions.outline(WHITE).strokeWidth(4));

        verifyIsIdenticalWithReferenceImage("drawCircle_thickStrokeSet_paintsUsingThickStroke.png");
    }

    @Test
    void drawCircle_fading_paintFadingCircle() {
        renderer.drawCircle(Offset.at(40, 20), 20, CircleDrawOptions.fading(RED));

        verifyIsIdenticalWithReferenceImage("drawCircle_fading_paintFadingCircle.png");
    }

    @Test
    void drawCircle_filled_paintsilledCircle() {
        renderer.drawCircle(Offset.at(40, 20), 20, CircleDrawOptions.filled(RED));

        verifyIsIdenticalWithReferenceImage("drawCircle_filled_paintsilledCircle.png");
    }

    @Test
    void drawSprite_assetRotatedAndTransparentAndFlipped_drawsSpriteOnlyInClip() {
        SpriteDrawOptions options = SpriteDrawOptions.originalSize().opacity(Percent.of(0.4)).rotation(degrees(20)).flipVertical(true).flipHorizontal(true);
        renderer.drawSprite(Asset.asset(Sprite::dummy16x16), Offset.at(4, 12), options);

        verifyIsIdenticalWithReferenceImage("drawSprite_assetRotatedAndTransparentAndFlipped_drawsSpriteOnlyInClip.png");
    }

    @Test
    void drawSprite_useClip_drawsSpriteOnlyInClip() {
        renderer.drawSprite(Sprite.dummy16x16(), Offset.at(4, 12), SpriteDrawOptions.scaled(2), new ScreenBounds(4, 4, 16, 16));

        verifyIsIdenticalWithReferenceImage("drawSprite_useClip_drawsSpriteOnlyInClip.png");
    }

    @Test
    void drawSprite_scaled_drawsSprite() {
        renderer.drawSprite(Sprite.dummy16x16(), Offset.at(4, 12), SpriteDrawOptions.scaled(2));

        verifyIsIdenticalWithReferenceImage("drawSprite_scaled_drawsSprite.png");
    }

    @Test
    void drawText_boldAlignedLeft_drawsText() {
        renderer.drawText(Offset.at(20, 10), "Test", TextDrawOptions.systemFont("Arial").bold().size(20));

        verifyNotAllPixelsAreBlack(); // fonts are system specific so pixelperfect compare is not applicable here
    }

    @Test
    void drawText_italicAlignedRight_drawsText() {
        renderer.drawText(Offset.at(20, 10), "Test", TextDrawOptions.systemFont("Arial").alignRight().italic().size(10).color(RED.opacity(0.8)));

        verifyNotAllPixelsAreBlack(); // fonts are system specific so pixelperfect compare is not applicable here
    }

    @Test
    void drawText_italicBoldAlignedCenter_drawsText() {
        renderer.drawText(Offset.at(20, 10), "Test", TextDrawOptions.systemFont("Arial").alignCenter().italic().bold().size(10).color(BLUE));

        verifyNotAllPixelsAreBlack(); // fonts are system specific so pixelperfect compare is not applicable here
    }

    @Test
    void drawText_normal_drawsText() {
        renderer.drawText(Offset.at(20, 10), "XXX", TextDrawOptions.systemFont("Arial"));

        verifyNotAllPixelsAreBlack(); // fonts are system specific so pixelperfect compare is not applicable here
    }

    private void verifyNotAllPixelsAreBlack() {
        long blackPixelCount = result.size().allPixels().stream()
                .map(pixel -> result.colorAt(pixel))
                .filter(color -> color.equals(Color.BLACK))
                .count();

        assertThat((long) result.size().allPixels().size()).isGreaterThan(blackPixelCount);
    }

    private void verifyIsIdenticalWithReferenceImage(String fileName) {
        Frame reference = Frame.fromFile("renderer/" + fileName);
        assertThat(result.listPixelDifferences(reference)).isEmpty();
    }
}
