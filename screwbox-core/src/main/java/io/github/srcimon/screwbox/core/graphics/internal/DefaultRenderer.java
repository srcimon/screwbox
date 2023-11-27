package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import static java.awt.RenderingHints.*;
import static java.util.Objects.nonNull;

public class DefaultRenderer implements Renderer {

    private static final float[] FADEOUT_FRACTIONS = new float[]{0.1f, 1f};
    private static final java.awt.Color FADEOUT_COLOR = AwtMapper.toAwtColor(Color.TRANSPARENT);

    private final Robot robot;
    private final WindowFrame frame;
    private Time lastUpdateTime = Time.now();
    private Graphics2D graphics;
    private Color lastUsedColor;

    public DefaultRenderer(final WindowFrame frame) {
        this.frame = frame;
        this.frame.setIgnoreRepaint(true);
        frame.getCanvas().createBufferStrategy(2);
        graphics = (Graphics2D) frame.getCanvas().getBufferStrategy().getDrawGraphics();
        try {
            robot = new Robot();
        } catch (final AWTException e) {
            throw new IllegalStateException("could not create robot for screenshots");
        }
    }

    @Override
    public void updateScreen(final boolean antialiased) {
        lastUpdateTime = Time.now();
        frame.getCanvas().getBufferStrategy().show();
        graphics.dispose();
        graphics = (Graphics2D) frame.getCanvas().getBufferStrategy().getDrawGraphics();
        lastUsedColor = null;
        if (antialiased) {
            graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        }
        fillWith(Color.BLACK);
    }

    @Override
    public void fillWith(final Color color) {
        applyNewColor(color);
        graphics.fillRect(0, 0, frame.getWidth(), frame.getHeight());
    }

    @Override
    public Sprite takeScreenshot() {
        int menuBarHeight = frame.getJMenuBar() == null ? 0 : frame.getJMenuBar().getHeight();
        final Rectangle rectangle = new Rectangle(frame.getX(),
                frame.getY() + frame.getInsets().top + menuBarHeight,
                frame.getCanvas().getWidth(),
                frame.canvasHeight());
        final BufferedImage screenCapture = robot.createScreenCapture(rectangle);
        return Sprite.fromImage(screenCapture);
    }

    private void applyOpacityConfig(final Percent opacity) {
        if (!opacity.isMaxValue()) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity.value()));
        }
    }

    private void resetOpacityConfig(final Percent opacity) {
        if (!opacity.isMaxValue()) {
            graphics.setComposite(AlphaComposite.SrcOver);
        }
    }

    @Override
    public void drawText(final Offset offset, final String text, final Font font, final Color color) {
        applyNewColor(color);
        graphics.setFont(AwtMapper.toAwtFont(font));

        graphics.drawString(text, offset.x(), offset.y());
    }

    @Override
    public void drawTextCentered(final Offset position, final String text, final Font font, final Color color) {
        final int textWidth = graphics.getFontMetrics(AwtMapper.toAwtFont(font)).stringWidth(text);
        final var offset = Offset.at(position.x() - textWidth / 2.0, position.y());
        drawText(offset, text, font, color);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percent opacity,
                           final Rotation rotation, final Flip flip, final ScreenBounds clipArea) {
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
            drawSpriteInContext(sprite, origin, scale, flip);
            graphics.rotate(-radians, x, y);
        } else {
            drawSpriteInContext(sprite, origin, scale, flip);
        }

        if (nonNull(clipArea)) {
            graphics.setClip(oldClip);
        }

        resetOpacityConfig(opacity);
    }

    private void drawSpriteInContext(final Sprite sprite, final Offset origin, final double scale,
                                     final Flip flip) {
        final Image image = sprite.image(lastUpdateTime);
        final AffineTransform transform = new AffineTransform();
        final Size size = sprite.size();
        final double xCorrect = flip.isHorizontal() ? scale * size.width() : 0;
        final double yCorrect = flip.isVertical() ? scale * size.height() : 0;
        transform.translate(origin.x() + xCorrect, origin.y() + yCorrect);
        transform.scale(scale * (flip.isHorizontal() ? -1 : 1), scale * (flip.isVertical() ? -1 : 1));
        graphics.drawImage(image, transform, frame);
    }

    @Override
    public void fillRectangle(final ScreenBounds bounds, final Color color) {
        applyNewColor(color);
        graphics.fillRect(
                bounds.offset().x(),
                bounds.offset().y(),
                bounds.size().width(),
                bounds.size().height());
    }

    @Override
    public void fillCircle(final Offset offset, final int diameter, final Color color) {
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
            graphics.setColor(AwtMapper.toAwtColor(color));
        }
    }

    @Override
    public void drawFadingCircle(Offset offset, int diameter, Color color) {
        var oldPaint = graphics.getPaint();
        var colors = new java.awt.Color[]{AwtMapper.toAwtColor(color), FADEOUT_COLOR};
        graphics.setPaint(new RadialGradientPaint(
                offset.x(),
                offset.y(),
                diameter / 2f,
                FADEOUT_FRACTIONS, colors));

        final int x = offset.x() - diameter / 2;
        final int y = offset.y() - diameter / 2;
        graphics.fillOval(x, y, diameter, diameter);
        graphics.setPaint(oldPaint);
    }

    @Override
    public void drawSprite(Supplier<Sprite> sprite, Offset origin, double scale, Percent opacity, Rotation rotation,
                           Flip flip, ScreenBounds clipArea) {
        drawSprite(sprite.get(), origin, scale, opacity, rotation, flip, clipArea);
    }

    @Override
    public void drawCircle(Offset offset, int diameter, Color color) {
        applyNewColor(color);
        final int x = offset.x() - diameter / 2;
        final int y = offset.y() - diameter / 2;
        graphics.drawOval(x, y, diameter, diameter);
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final Color color) {
        applyNewColor(color);
        graphics.drawRect(offset.x(), offset.y(), size.width(), size.height());
    }

}