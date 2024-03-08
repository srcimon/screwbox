package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public class DefaultRenderer implements Renderer {

    private static final float[] FADEOUT_FRACTIONS = new float[]{0.1f, 1f};
    private static final java.awt.Color FADEOUT_COLOR = AwtMapper.toAwtColor(Color.TRANSPARENT);

    private Time lastUpdateTime = Time.now();
    private Size canvasSize;
    private Graphics2D graphics;
    private Color lastUsedColor;

    @Override
    public void updateGraphicsContext(final Supplier<Graphics2D> graphicsSupplier, final Size canvasSize) {
        lastUpdateTime = Time.now();
        this.canvasSize = canvasSize;
        this.graphics = graphicsSupplier.get();
        lastUsedColor = null;
        fillWith(Color.BLACK);
    }

    @Override
    public void fillWith(final Color color) {
        applyNewColor(color);
        graphics.fillRect(0, 0, canvasSize.width(), canvasSize.height());
    }

    private void applyOpacityConfig(final Percent opacity) {
        if (!opacity.isMax()) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity.value()));
        }
    }

    private void resetOpacityConfig(final Percent opacity) {
        if (!opacity.isMax()) {
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
                           final Rotation rotation, final Flip flip, final ScreenBounds clip) {
        applyOpacityConfig(opacity);

        final var oldClip = graphics.getClip();
        if (nonNull(clip)) {
            graphics.setClip(clip.offset().x(), clip.offset().y(), clip.size().width(),
                    clip.size().height());
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

        if (nonNull(clip)) {
            graphics.setClip(oldClip);
        }

        resetOpacityConfig(opacity);
    }

    private void drawSpriteInContext(final Sprite sprite, final Offset origin, final double scale, final Flip flip) {
        final Image image = sprite.image(lastUpdateTime);
        final AffineTransform transform = new AffineTransform();
        final Size size = sprite.size();
        final double xCorrect = flip.isHorizontal() ? scale * size.width() : 0;
        final double yCorrect = flip.isVertical() ? scale * size.height() : 0;
        transform.translate(origin.x() + xCorrect, origin.y() + yCorrect);
        transform.scale(scale * (flip.isHorizontal() ? -1 : 1), scale * (flip.isVertical() ? -1 : 1));
        graphics.drawImage(image, transform, null);
    }

    @Override
    public void fillCircle(final Offset offset, final int diameter, final Color color) {
        applyNewColor(color);
        final int x = offset.x() - diameter / 2;
        final int y = offset.y() - diameter / 2;
        graphics.fillOval(x, y, diameter, diameter);
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
                           Flip flip, ScreenBounds clip) {
        drawSprite(sprite.get(), origin, scale, opacity, rotation, flip, clip);
    }

    @Override
    public void drawCircle(Offset offset, int diameter, Color color, int strokeWidth) {
        applyNewColor(color);
        final int x = offset.x() - diameter / 2;
        final int y = offset.y() - diameter / 2;
        if (strokeWidth == 1) {
            graphics.drawOval(x, y, diameter, diameter);
        } else {
            var oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(strokeWidth));
            graphics.drawOval(x, y, diameter, diameter);
            graphics.setStroke(oldStroke);
        }
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        applyNewColor(options.color());

        if (options.rotation().isNone()) {
            if (options.isFilled()) {
                graphics.fillRect(offset.x(), offset.y(), size.width(), size.height());
            } else {
                final var oldStroke = graphics.getStroke();
                graphics.setStroke(new BasicStroke(options.strokeWidth()));
                graphics.drawRect(offset.x(), offset.y(), size.width(), size.height());
                graphics.setStroke(oldStroke);
            }
        } else {
            final double x = offset.x() + size.width() / 2.0;
            final double y = offset.y() + size.height() / 2.0;
            final double radians = options.rotation().radians();
            graphics.rotate(radians, x, y);
            if (options.isFilled()) {
                graphics.fillRect(offset.x(), offset.y(), size.width(), size.height());
            } else {
                final var oldStroke = graphics.getStroke();
                graphics.setStroke(new BasicStroke(options.strokeWidth()));
                graphics.drawRect(offset.x(), offset.y(), size.width(), size.height());
                graphics.setStroke(oldStroke);
            }
            graphics.rotate(-radians, x, y);
        }
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        applyNewColor(options.color());
        if (options.strokeWidth() == 1) {
            graphics.drawLine(from.x(), from.y(), to.x(), to.y());
        } else {
            var oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(options.strokeWidth()));
            graphics.drawLine(from.x(), from.y(), to.x(), to.y());
            graphics.setStroke(oldStroke);
        }

    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        // FILL
        applyNewColor(color);
        final int x = offset.x() - diameter / 2;
        final int y = offset.y() - diameter / 2;
        graphics.fillOval(x, y, diameter, diameter);

        // FADING
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

        //OUTLINE
        applyNewColor(color);
        final int x = offset.x() - diameter / 2;
        final int y = offset.y() - diameter / 2;
        if (strokeWidth == 1) {
            graphics.drawOval(x, y, diameter, diameter);
        } else {
            var oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(strokeWidth));
            graphics.drawOval(x, y, diameter, diameter);
            graphics.setStroke(oldStroke);
        }
    }

}