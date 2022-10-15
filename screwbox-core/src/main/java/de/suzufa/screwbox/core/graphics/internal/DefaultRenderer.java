package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;
import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtFont;
import static java.util.Objects.nonNull;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class DefaultRenderer implements Renderer {

    private final Robot robot;
    private final Frame frame;
    private Time lastUpdateTime = Time.now();
    private Graphics2D graphics;
    private Color lastUsedColor;

    public DefaultRenderer(final Frame frame) {
        this.frame = frame;
        this.frame.setIgnoreRepaint(true);
        graphics = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        initializeFontDrawing();
        try {
            robot = new Robot();
        } catch (final AWTException e) {
            throw new IllegalStateException("could not create robot for screenshots");
        }
    }

    @Override
    public void updateScreen(final boolean antialiased) {
//        BufferedImage lightmapImage = new BufferedImage(frame.getWidth(), frame.getHeight(),
//                BufferedImage.TYPE_INT_ARGB);
//
//        Graphics2D lightmapGraphics = (Graphics2D) lightmapImage.getGraphics();
//        Polygon polygon = new Polygon();
//        polygon.addPoint(120, 120);
//        polygon.addPoint(240, 215);
//        polygon.addPoint(205, 208);
//        polygon.addPoint(42, 130);
//
//        Polygon polygon2 = new Polygon();
//        polygon2.addPoint(50, 80);
//        polygon2.addPoint(400, 80);
//        polygon2.addPoint(404, 208);
//        polygon2.addPoint(10, 40);
//
//        java.awt.Color[] colors = new java.awt.Color[2];
//        colors[0] = toAwtColor(Color.BLACK);
//        colors[1] = toAwtColor(Color.TRANSPARENT);
//        float[] fractions = new float[2];
//        fractions[0] = 0.1f;
//        fractions[1] = 1f;
//        RadialGradientPaint paint = new RadialGradientPaint(120, 120, 40, fractions, colors);
//        lightmapGraphics.setPaint(paint);
//        lightmapGraphics.fillPolygon(polygon);
//        RadialGradientPaint paint2 = new RadialGradientPaint(120, 120, 140, fractions, colors);
//        lightmapGraphics.setPaint(paint2);
//        lightmapGraphics.fillPolygon(polygon2);
//        Image result = ImageUtil.applyFilter(lightmapImage, new InvertAlphaFilter());
//
//        graphics.drawImage(result, 0, 0, null);

        // ==============
        lastUpdateTime = Time.now();
        frame.getBufferStrategy().show();
        graphics.dispose();
        graphics = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        lastUsedColor = null;
        if (antialiased) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    private void initializeFontDrawing() {
        drawText(Offset.origin(), "-", new Font("Arial", 1), Color.WHITE);
    }

    @Override
    public void fillWith(final Color color) {
        applyNewColor(color);
        graphics.fillRect(0, 0, frame.getWidth(), frame.getHeight());
    }

    @Override
    public Sprite takeScreenshot() {
        final Rectangle rectangle = new Rectangle(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
        final BufferedImage screenCapture = robot.createScreenCapture(rectangle);
        return Sprite.fromImage(screenCapture);
    }

    private void applyOpacityConfig(final Percentage opacity) {
        if (!opacity.isMaxValue()) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity.valueFloat()));
        }
    }

    private void resetOpacityConfig(final Percentage opacity) {
        if (!opacity.isMaxValue()) {
            graphics.setComposite(AlphaComposite.SrcOver);
        }
    }

    @Override
    public void drawText(final Offset offset, final String text, final Font font, final Color color) {
        applyNewColor(color);
        graphics.setFont(toAwtFont(font));

        graphics.drawString(text, offset.x(), offset.y());
    }

    @Override
    public void drawTextCentered(final Offset position, final String text, final Font font, final Color color) {
        final int textWidth = graphics.getFontMetrics(toAwtFont(font)).stringWidth(text);
        final var offset = Offset.at(position.x() - textWidth / 2.0, position.y());
        drawText(offset, text, font, color);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percentage opacity,
            final Angle rotation, final FlipMode flipMode, final WindowBounds clipArea) {
        applyOpacityConfig(opacity);

        final var oldClip = graphics.getClip();
        if (nonNull(clipArea)) {
            graphics.setClip(clipArea.offset().x(), clipArea.offset().y(), clipArea.size().width(),
                    clipArea.size().height());
        }
        if (!rotation.isNone()) {
            final double x = origin.x() + sprite.size().width() * scale / 2.0;
            final double y = origin.y() + sprite.size().height() * scale / 2.0;
            final double radians = rotation.radians();
            graphics.rotate(radians, x, y);
            drawSpriteInContext(sprite, origin, scale, flipMode);
            graphics.rotate(-radians, x, y);
        } else {
            drawSpriteInContext(sprite, origin, scale, flipMode);
        }

        if (nonNull(clipArea)) {
            graphics.setClip(oldClip);
        }

        resetOpacityConfig(opacity);
    }

    private void drawSpriteInContext(final Sprite sprite, final Offset origin, final double scale,
            final FlipMode flipMode) {
        final Image image = sprite.getImage(lastUpdateTime);
        final AffineTransform transform = new AffineTransform();
        final Dimension size = sprite.size();
        final double xCorrect = flipMode.isHorizontal() ? scale * size.width() : 0;
        final double yCorrect = flipMode.isVertical() ? scale * size.height() : 0;
        transform.translate(origin.x() + xCorrect, origin.y() + yCorrect);
        transform.scale(scale * (flipMode.isHorizontal() ? -1 : 1), scale * (flipMode.isVertical() ? -1 : 1));
        graphics.drawImage(image, transform, frame);
    }

    @Override
    public void drawRectangle(final WindowBounds bounds, final Color color) {
        applyNewColor(color);
        graphics.fillRect(
                bounds.offset().x(),
                bounds.offset().y(),
                bounds.size().width(),
                bounds.size().height());
    }

    @Override
    public void drawCircle(final Offset offset, final int diameter, final Color color) {
        applyNewColor(color);
        final int x = offset.x() - diameter / 2;
        final int y = offset.y() - diameter / 2;
        graphics.fillOval(x, y, diameter, diameter);
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final Color color) {
        applyNewColor(color);
        graphics.drawLine(from.x(), from.y(), to.x(), to.y());
    }

    private void applyNewColor(final Color color) {
        if (lastUsedColor != color) {
            lastUsedColor = color;
            graphics.setColor(toAwtColor(color));
        }
    }

}
