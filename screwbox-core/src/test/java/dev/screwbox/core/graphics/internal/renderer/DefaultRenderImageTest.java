package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static dev.screwbox.core.Angle.degrees;
import static dev.screwbox.core.Percent.half;
import static dev.screwbox.core.graphics.Color.BLUE;
import static dev.screwbox.core.graphics.Color.ORANGE;
import static dev.screwbox.core.graphics.Color.RED;
import static dev.screwbox.core.graphics.Color.WHITE;
import static dev.screwbox.core.graphics.options.LineDrawOptions.color;
import static dev.screwbox.core.graphics.options.RectangleDrawOptions.fading;
import static dev.screwbox.core.graphics.options.RectangleDrawOptions.filled;
import static dev.screwbox.core.graphics.options.RectangleDrawOptions.outline;
import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class DefaultRenderImageTest {

    private static final Sprite SPRITE = SpriteBundle.DOT_BLUE.get();
    private static final ScreenBounds CLIP = new ScreenBounds(0, 0, 80, 40);

    Frame result;
    DefaultRenderer renderer;

    @BeforeEach
    void beforeEach() {
        BufferedImage image = ImageOperations.createImage(80, 40);
        result = Frame.fromImage(image);
        Graphics2D graphics = image.createGraphics();

        renderer = new DefaultRenderer();
        renderer.updateContext(() -> graphics);
        renderer.fillWith(Color.BLACK, CLIP);
    }

    @Test
    void fillWith_colorRed_fillsWholeImageWithRed() {
        renderer.fillWith(RED, CLIP);

        verifyIsSameImage(result.image(), "renderer/fillWith_colorRed_fillsWholeImageWithRed.png");
    }

    @Test
    void drawRectangle_colorBlue_fillsRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(4, 4), filled(BLUE), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawRectangle_colorBlue_fillsRectangleBlue.png");
    }

    @Test
    void drawRectangle_rotated_fillsRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(20, 40), filled(BLUE).rotation(degrees(45)), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawRectangle_rotated_fillsRectangleBlue.png");
    }

    @Test
    void drawRectangle_rounded_fillsRoundedRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(10, 20), filled(BLUE).rotation(degrees(45)).curveRadius(8), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawRectangle_rounded_fillsRoundedRectangleBlue.png");
    }

    @Test
    void drawRectangle_fadingNoCurveRadius_drawsFilledRectangle() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(50, 30), fading(BLUE), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawRectangle_fadingNoCurveRadius_drawsFilledRectangle.png");
    }

    @Test
    void drawRectangle_fading_drawsFadingRectangle() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(50, 30), fading(BLUE).curveRadius(14), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawRectangle_fading_drawsFadingRectangle.png");
    }

    @Test
    void drawRectangle_rotatedOutline_outlineRectangleBlue() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(20, 40), outline(BLUE).strokeWidth(3).rotation(degrees(45)), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawRectangle_rotatedOutline_outlineRectangleBlue.png");
    }

    @Test
    void drawRectangle_halfOpacity_appliesOpacityChanges() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(4, 4), filled(BLUE.opacity(half())), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawRectangle_halfOpacity_appliesOpacityChanges.png");
    }

    @Test
    void drawRectangle_outline_onlyPaintsOutline() {
        renderer.drawRectangle(Offset.at(10, 10), Size.of(10, 10), RectangleDrawOptions.outline(BLUE).strokeWidth(2), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawRectangle_outline_onlyPaintsOutline.png");
    }

    @Test
    void drawLine_usingThickStroke_paintsUsingThickStroke() {
        renderer.drawLine(Offset.at(4, 5), Offset.at(20, 20), color(ORANGE).strokeWidth(2), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_usingThickStroke_paintsUsingThickStroke.png");
    }

    @Test
    void drawLine_noStrokeSet_paintsUsingThinStroke() {
        renderer.drawLine(Offset.at(8, 1), Offset.at(40, 30), color(ORANGE), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawLine_noStrokeSet_paintsUsingThinStroke.png");
    }

    @Test
    void drawOval_noStrokeSet_paintsUsingThinStroke() {
        renderer.drawOval(Offset.at(10, 10), 4, 4, OvalDrawOptions.outline(WHITE), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_noStrokeSet_paintsUsingThinStroke.png");
    }

    @Test
    void drawOval_notACircle_drawsOval() {
        renderer.drawOval(Offset.at(10, 10), 8, 3, OvalDrawOptions.outline(WHITE), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_notACircle_drawsOval.png");
    }

    @Test
    void drawOval_thickStrokeSet_paintsUsingThickStroke() {
        renderer.drawOval(Offset.at(20, 20), 8, 8, OvalDrawOptions.outline(WHITE).strokeWidth(4), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_thickStrokeSet_paintsUsingThickStroke.png");
    }

    @Test
    void drawOval_fading_paintFadingCircle() {
        renderer.drawOval(Offset.at(40, 20), 20, 20, OvalDrawOptions.fading(RED), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_fading_paintFadingCircle.png");
    }

    @Test
    void drawOval_fadingArc_paintFadingArc() {
        renderer.drawOval(Offset.at(40, 20), 20, 20, OvalDrawOptions.fading(RED).startAngle(Angle.degrees(45)).arcAngle(Angle.degrees(90)), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_fadingArc_paintsFadingArc.png");
    }

    @Test
    void drawOval_filledArc_paintFilledArc() {
        renderer.drawOval(Offset.at(40, 20), 20, 20, OvalDrawOptions.filled(RED).startAngle(Angle.degrees(-45)).arcAngle(Angle.degrees(120)), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_filledArc_paintsFilledArc.png");
    }

    @Test
    void drawOval_outlineArc_paintsOutlineArc() {
        renderer.drawOval(Offset.at(40, 20), 10, 10, OvalDrawOptions.outline(RED).arcAngle(Angle.degrees(180)), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_outlineArc_paintsOutlineArc.png");
    }

    @Test
    void drawOval_filled_paintsFilledCircle() {
        renderer.drawOval(Offset.at(40, 20), 20, 20, OvalDrawOptions.filled(RED), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawOval_filled_paintsFilledCircle.png");
    }

    @Test
    void drawSprite_assetRotatedAndTransparentAndFlipped_drawsSpriteOnlyInClip() {
        SpriteDrawOptions options = SpriteDrawOptions.originalSize().opacity(Percent.of(0.4)).rotation(degrees(20)).flipVertical(true).flipHorizontal(true);
        renderer.drawSprite(SpriteBundle.DOT_BLUE, Offset.at(4, 12), options, CLIP);

        verifyIsSameImage(result.image(), "renderer/drawSprite_assetRotatedAndTransparentAndFlipped_drawsSpriteOnlyInClip.png");
    }

    @Test
    void drawSprite_spriteHasSpin_drawsSpriteWithSpin() {
        renderer.drawSprite(SpriteBundle.ICON, Offset.at(40, 12), SpriteDrawOptions.originalSize().spin(Percent.of(0.8)), CLIP);
        renderer.drawSprite(SpriteBundle.ICON, Offset.at(10, 12), SpriteDrawOptions.scaled(0.4).flipVertical(true).spin(Percent.of(0.6)).spinHorizontal(false), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawSprite_spriteHasSpin_drawsSpriteWithSpin.png");
    }

    @Test
    void drawSprite_scaled_drawsSprite() {
        renderer.drawSprite(SPRITE, Offset.at(4, 12), SpriteDrawOptions.scaled(2), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawSprite_scaled_drawsSprite.png");
    }

    @Test
    void drawText_boldAlignedLeft_drawsText() {
        renderer.drawText(Offset.at(20, 10), "Test", SystemTextDrawOptions.systemFont("Arial").bold().size(20), CLIP);

        verifyNotAllPixelsAreBlack(); // fonts are system specific so pixel perfect compare is not applicable here
    }

    @Test
    void drawText_italicAlignedRight_drawsText() {
        renderer.drawText(Offset.at(20, 10), "Test", SystemTextDrawOptions.systemFont("Arial").alignRight().italic().size(10).color(RED.opacity(0.8)), CLIP);

        verifyNotAllPixelsAreBlack(); // fonts are system specific so pixel perfect compare is not applicable here
    }

    @Test
    void drawText_italicBoldAlignedCenter_drawsText() {
        renderer.drawText(Offset.at(20, 10), "Test", SystemTextDrawOptions.systemFont("Arial").alignCenter().italic().bold().size(10).color(BLUE), CLIP);

        verifyNotAllPixelsAreBlack(); // fonts are system specific so pixel perfect compare is not applicable here
    }

    @Test
    void drawText_normal_drawsText() {
        renderer.drawText(Offset.at(20, 10), "XXX", SystemTextDrawOptions.systemFont("Arial"), CLIP);

        verifyNotAllPixelsAreBlack(); // fonts are system specific so pixel perfect compare is not applicable here
    }

    @Test
    void fillWith_spriteFillWithDoubleSizeAndHalfOpacity_fillsWholeImage() {
        renderer.fillWith(SPRITE, SpriteFillOptions.scale(2).opacity(Percent.half()), CLIP);

        verifyIsSameImage(result.image(), "renderer/fillWith_spriteFillWithDoubleSizeAndHalfOpacity_fillsWholeImage.png");
    }

    @Test
    void fillWith_spriteFillWithOffset_fillsWholeImage() {
        renderer.fillWith(SPRITE, SpriteFillOptions.scale(1).offset(Offset.at(3, 5)).opacity(Percent.half()), CLIP);

        verifyIsSameImage(result.image(), "renderer/fillWith_spriteFillWithOffset_fillsWholeImage.png");
    }

    @Test
    void fillWith_spriteFillWithDifferentOffset_fillsWholeImage() {
        renderer.fillWith(SPRITE, SpriteFillOptions.scale(1).offset(Offset.at(-13, -9000)).opacity(Percent.half()), CLIP);

        verifyIsSameImage(result.image(), "renderer/fillWith_spriteFillWithDifferentOffset_fillsWholeImage.png");
    }

    @Test
    void drawText_boldzillaFontScaledWithPaddingUsingUppercaseChars_drawsText() {
        renderer.drawText(Offset.at(10, 10), "hi there", TextDrawOptions.font(FontBundle.BOLDZILLA).scale(1).padding(1).uppercase(), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawText_boldzillaFontScaledWithPaddingUsingUppercaseChars_drawsText.png");
    }

    @Test
    void drawText_boldzillaFontCenteredWithOpacity_drawsText() {
        renderer.drawText(Offset.at(10, 10), "test", TextDrawOptions.font(FontBundle.BOLDZILLA).alignCenter().opacity(Percent.half()), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawText_boldzillaFontCenteredWithOpacity_drawsText.png");
    }

    @Test
    void drawText_boldzillaAlignedRight_drawsText() {
        renderer.drawText(Offset.at(60, 10), "TEST", TextDrawOptions.font(FontBundle.BOLDZILLA).alignRight(), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawText_boldzillaAlignedRight_drawsText.png");

    }

    @Test
    void drawText_multilineDrawsText() {
        renderer.drawText(Offset.at(60, 10), "TheFox", TextDrawOptions.font(FontBundle.SKINNY_SANS).charactersPerLine(3).alignRight(), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawText_multilineDrawsText.png");
    }

    @Test
    void drawSprite_defaultShaderSet_drawsUsingDefaultShader() {
        renderer.setDefaultShader(ShaderBundle.GREYSCALE.get());
        renderer.drawSprite(SpriteBundle.BOX, Offset.origin(), SpriteDrawOptions.originalSize(), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawSprite_defaultShaderSet_drawsUsingDefaultShader.png");
    }

    @Test
    void drawSprite_customShaderSet_drawsUsingCustomShader() {
        renderer.drawSprite(SpriteBundle.BOX, Offset.origin(), SpriteDrawOptions.originalSize().shaderSetup(ShaderBundle.INVERT_COLORS), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawSprite_customShaderSet_drawsUsingCustomShader.png");
    }

    @Test
    void drawPolygon_filled_drawsFilledPolygon() {
        renderer.drawPolygon(List.of(Offset.at(10, 4), Offset.at(40, 4), Offset.at(20, 10), Offset.at(4, 15)), PolygonDrawOptions.filled(RED), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawPolygon_filled_drawsFilledPolygon.png");
    }

    @Test
    void drawPolygon_outline_drawsOutlinePolygon() {
        renderer.drawPolygon(List.of(Offset.at(10, 4), Offset.at(40, 4), Offset.at(20, 10), Offset.at(4, 15)), PolygonDrawOptions.outline(RED), CLIP);
        renderer.drawPolygon(List.of(Offset.at(10, 20), Offset.at(40, 25), Offset.at(20, 40), Offset.at(4, 80)), PolygonDrawOptions.outline(RED).strokeWidth(2), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawPolygon_outline_drawsOutlinePolygon.png");
    }

    @Test
    void drawPolygon_useHorizontalSmoothing_drawsOutlinePolygon() {
        renderer.drawPolygon(List.of(Offset.at(10, 4), Offset.at(20, 8), Offset.at(30, 12), Offset.at(40, 4), Offset.at(60, 20)), PolygonDrawOptions.outline(RED)
            .smoothing(PolygonDrawOptions.Smoothing.HORIZONTAL), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawPolygon_useHorizontalSmoothing_drawsOutlinePolygon.png");
    }

    @Test
    void drawPolygon_useSplineSmoothing_drawsOutlinePolygon() {
        renderer.drawPolygon(List.of(Offset.at(10, 4), Offset.at(20, 8), Offset.at(30, 12), Offset.at(40, 4), Offset.at(60, 20), Offset.at(40, 30)), PolygonDrawOptions.outline(RED)
            .smoothing(PolygonDrawOptions.Smoothing.SPLINE), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawPolygon_useSplineSmoothing_drawsOutlinePolygon.png");
    }

    @Test
    void drawPolygon_useSplineSmoothingAndConnected_drawsOutlinePolygon() {
        renderer.drawPolygon(List.of(Offset.at(10, 10), Offset.at(20, 8), Offset.at(30, 12), Offset.at(40, 4), Offset.at(60, 20), Offset.at(40, 30), Offset.at(10, 10)), PolygonDrawOptions.outline(RED)
            .smoothing(PolygonDrawOptions.Smoothing.SPLINE), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawPolygon_useSplineSmoothingAndConnected_drawsOutlinePolygon.png");
    }

    @Test
    void drawPolygon_verticalGradient_usesGradient() {
        renderer.drawPolygon(List.of(Offset.at(10, 4), Offset.at(40, 4), Offset.at(20, 10), Offset.at(4, 15)), PolygonDrawOptions.verticalGradient(RED, BLUE), CLIP);

        verifyIsSameImage(result.image(), "renderer/drawPolygon_verticalGradient_usesGradient.png");
    }


    @Test
    void drawSprite_defaultAndCustomShaderSet_drawsUsingCombinedShader() {
        renderer.setDefaultShader(ShaderBundle.GREYSCALE.get());

        renderer.drawSprite(SpriteBundle.BOX, Offset.origin(), SpriteDrawOptions.originalSize().shaderSetup(ShaderBundle.INVERT_COLORS), CLIP);
        verifyIsSameImage(result.image(), "renderer/drawSprite_defaultAndCustomShaderSet_drawsUsingCombinedShader.png");
    }

    private void verifyNotAllPixelsAreBlack() {
        long blackPixelCount = result.size().all().stream()
            .map(pixel -> result.colorAt(pixel))
            .filter(color -> color.equals(Color.BLACK))
            .count();

        assertThat((long) result.size().all().size()).isGreaterThan(blackPixelCount);
    }

}
