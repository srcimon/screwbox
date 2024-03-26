package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Supplier;

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

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options) {
        applyNewColor(options.color());
        final var font = toAwtFont(options);
        final var fontMetrics = graphics.getFontMetrics(font);
        final int y = (int) (offset.y() + fontMetrics.getHeight() / 2.0);
        graphics.setFont(font);
        if (TextDrawOptions.Alignment.LEFT.equals(options.alignment())) {
            graphics.drawString(text, offset.x(), y);
        } else {
            final int textWidth = fontMetrics.stringWidth(text);
            final int xDelta = TextDrawOptions.Alignment.CENTER.equals(options.alignment()) ? textWidth / 2 : textWidth;
            graphics.drawString(text, offset.x() - xDelta, y);
        }
    }

    private java.awt.Font toAwtFont(final TextDrawOptions options) {
        final int value = options.bold() ? java.awt.Font.BOLD : java.awt.Font.ROMAN_BASELINE;
        final int realValue = options.italic() ? value + java.awt.Font.ITALIC : value;
        return new java.awt.Font(options.fontName(), realValue, options.size());
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

    private void drawSpriteInContext(final Sprite sprite, final Offset origin, SpriteDrawOptions options) {
        final Image image = sprite.image(lastUpdateTime);
        final AffineTransform transform = new AffineTransform();
        final Size size = sprite.size();
        final double xCorrect = options.isFlipHorizontal() ? options.scale() * size.width() : 0;
        final double yCorrect = options.isFlipVertical() ? options.scale() * size.height() : 0;
        transform.translate(origin.x() + xCorrect, origin.y() + yCorrect);
        transform.scale(options.scale() * (options.isFlipHorizontal() ? -1 : 1), options.scale() * (options.isFlipVertical() ? -1 : 1));
        graphics.drawImage(image, transform, null);
    }

    private void applyNewColor(final Color color) {
        if (lastUsedColor != color) {
            lastUsedColor = color;
            graphics.setColor(AwtMapper.toAwtColor(color));
        }
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        applyNewColor(options.color());

        if (options.rotation().isNone()) {
            if (options.style() == RectangleDrawOptions.Style.FILLED) {
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
            if (options.style() == RectangleDrawOptions.Style.FILLED) {
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
        final int x = offset.x() - radius;
        final int y = offset.y() - radius;
        final int diameter = radius * 2;

        if (options.style() == CircleDrawOptions.Style.FILLED) {
            applyNewColor(options.color());
            graphics.fillOval(x, y, diameter, diameter);
        } else if (options.style() == CircleDrawOptions.Style.FADING) {
            final var oldPaint = graphics.getPaint();
            final var colors = new java.awt.Color[]{AwtMapper.toAwtColor(options.color()), FADEOUT_COLOR};
            graphics.setPaint(new RadialGradientPaint(
                    offset.x(),
                    offset.y(),
                    radius,
                    FADEOUT_FRACTIONS, colors));

            graphics.fillOval(x, y, diameter, diameter);
            graphics.setPaint(oldPaint);
        } else {
            applyNewColor(options.color());
            if (options.strokeWidth() == 1) {
                graphics.drawOval(x, y, diameter, diameter);
            } else {
                var oldStroke = graphics.getStroke();
                graphics.setStroke(new BasicStroke(options.strokeWidth()));
                graphics.drawOval(x, y, diameter, diameter);
                graphics.setStroke(oldStroke);
            }
        }
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        drawSprite(sprite.get(), origin, options);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        applyOpacityConfig(options.opacity());

        if (!options.rotation().isNone()) {
            final double x = origin.x() + sprite.size().width() * options.scale() / 2.0;
            final double y = origin.y() + sprite.size().height() * options.scale() / 2.0;
            final double radians = options.rotation().radians();
            graphics.rotate(radians, x, y);
            drawSpriteInContext(sprite, origin, options);
            graphics.rotate(-radians, x, y);
        } else {
            drawSpriteInContext(sprite, origin, options);
        }

        resetOpacityConfig(options.opacity());
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {

        final var oldClip = graphics.getClip();
        graphics.setClip(
                clip.offset().x(),
                clip.offset().y(),
                clip.size().width(),
                clip.size().height());

        drawSprite(sprite, origin, options);

        graphics.setClip(oldClip);

    }

}