package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import io.github.srcimon.screwbox.core.utils.TextUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

public class DefaultRenderer implements Renderer {

    private static final float[] FADEOUT_FRACTIONS = new float[]{0.0f, 0.3f, 0.6f, 1f};
    private static final java.awt.Color FADEOUT_COLOR = toAwtColor(Color.TRANSPARENT);

    private Time lastUpdateTime = Time.now();
    private Graphics2D graphics;
    private Color lastUsedColor;
    private ScreenBounds lastUsedClip;

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        lastUpdateTime = Time.now();
        this.graphics = graphics.get();
        lastUsedColor = null;
        lastUsedClip = null;
    }

    @Override
    public void fillWith(final Color color, final ScreenBounds clip) {
        applyClip(clip);
        applyNewColor(color);
        graphics.fillRect(clip.offset().x(), clip.offset().y(), clip.width(), clip.height());
    }

    @Override
    public void rotate(final Rotation rotation, final ScreenBounds clip, final Color backgroundColor) {
        // not invoking fillWith(color) here to prevent setting clip
        applyNewColor(backgroundColor);
        graphics.fillRect(clip.offset().x(), clip.offset().y(), clip.width(), clip.height());
        graphics.rotate(rotation.radians(), clip.width() / 2.0, clip.height() / 2.0);
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options, final ScreenBounds clip) {
        applyClip(clip);
        applyOpacityConfig(options.opacity());
        final int spriteWidth = (int) (sprite.width() * options.scale());
        final int spriteHeight = (int) (sprite.height() * options.scale());
        final int xStart = options.offset().x() % spriteWidth == 0 ? 0 : options.offset().x() % spriteWidth - spriteWidth;
        final int yStart = options.offset().y() % spriteHeight == 0 ? 0 : options.offset().y() % spriteHeight - spriteHeight;
        for (int x = xStart; x <= clip.width() + clip.offset().x(); x += spriteWidth) {
            for (int y = yStart; y <= clip.height() + clip.offset().y(); y += spriteHeight) {
                final AffineTransform transform = new AffineTransform();
                transform.translate(x, y);
                transform.scale(options.scale(), options.scale());
                graphics.drawImage(sprite.image(lastUpdateTime), transform, null);
            }
        }
        resetOpacityConfig(options.opacity());
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        applyClip(clip);
        applyNewColor(options.color());
        final var font = toAwtFont(options);
        final var fontMetrics = graphics.getFontMetrics(font);
        final int y = (int) (offset.y() + fontMetrics.getHeight() / 2.0);
        graphics.setFont(font);
        if (SystemTextDrawOptions.Alignment.LEFT.equals(options.alignment())) {
            graphics.drawString(text, offset.x(), y);
        } else {
            final int textWidth = fontMetrics.stringWidth(text);
            final int xDelta = SystemTextDrawOptions.Alignment.CENTER.equals(options.alignment()) ? textWidth / 2 : textWidth;
            graphics.drawString(text, offset.x() - xDelta, y);
        }
    }

    private java.awt.Font toAwtFont(final SystemTextDrawOptions options) {
        final int value = options.isBold() ? java.awt.Font.BOLD : java.awt.Font.ROMAN_BASELINE;
        final int realValue = options.isItalic() ? value + java.awt.Font.ITALIC : value;
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

    private void drawSpriteInContext(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        final Image image = sprite.image(lastUpdateTime);
        final AffineTransform transform = new AffineTransform();
        final Size size = sprite.size();
        final double xCorrect = options.isFlipHorizontal() ? options.scale() * size.width() : 0;
        final double yCorrect = options.isFlipVertical() ? options.scale() * size.height() : 0;

        if (options.spin().isZero()) {
            transform.translate(origin.x() + xCorrect, origin.y() + yCorrect);
        } else {
            double distort = Ease.SINE_IN_OUT.applyOn(options.spin()).value() * -2 + 1;
            if (options.isSpinHorizontal()) {
                transform.translate(origin.x() + options.scale() * size.width() / 2.0, origin.y());
                transform.scale(distort, 1);
                transform.translate(options.scale() * size.width() / -2.0 + xCorrect, yCorrect);
            } else {
                transform.translate(origin.x(), origin.y() + options.scale() * size.height() / 2.0);
                transform.scale(1, distort);
                transform.translate(xCorrect, options.scale() * size.height() / -2.0 + yCorrect);
            }
        }

        transform.scale(options.scale() * (options.isFlipHorizontal() ? -1 : 1), options.scale() * (options.isFlipVertical() ? -1 : 1));
        graphics.drawImage(image, transform, null);
    }

    private void applyNewColor(final Color color) {
        if (lastUsedColor != color) {
            lastUsedColor = color;
            graphics.setColor(toAwtColor(color));
        }
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        applyNewColor(options.color());
        applyClip(clip);

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
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        applyNewColor(options.color());
        applyClip(clip);
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
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options, final ScreenBounds clip) {
        applyClip(clip);
        final int x = offset.x() - radius;
        final int y = offset.y() - radius;
        final int diameter = radius * 2;

        if (options.style() == CircleDrawOptions.Style.FILLED) {
            applyNewColor(options.color());
            graphics.fillOval(x, y, diameter, diameter);
        } else if (options.style() == CircleDrawOptions.Style.FADING) {
            final var oldPaint = graphics.getPaint();
            Color color = options.color();
            final var colors = new java.awt.Color[]{
                    toAwtColor(color),
                    toAwtColor(color.opacity(color.opacity().value() / 2.0)),
                    toAwtColor(color.opacity(color.opacity().value() / 4.0)),
                    FADEOUT_COLOR
            };

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
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        drawSprite(sprite.get(), origin, options, clip);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        applyClip(clip);
        applyOpacityConfig(options.opacity());

        if (!options.rotation().isNone()) {
            final double x = origin.x() + sprite.width() * options.scale() / 2.0;
            final double y = origin.y() + sprite.height() * options.scale() / 2.0;
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
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        applyClip(clip);
        applyOpacityConfig(options.opacity());
        int y = 0;
        for (final String line : TextUtil.lineWrap(text, options.charactersPerLine())) {
            final List<Sprite> allSprites = options.font().spritesFor(options.isUppercase() ? line.toUpperCase() : line);
            double x = offset.x() + switch (options.alignment()) {
                case LEFT -> 0;
                case CENTER -> -options.widthOf(line) / 2.0;
                case RIGHT -> -options.widthOf(line);
            };
//TODO FIXME
            //TODO changelog fix text scaling
            for (final var sprite : allSprites) {
                final Image image = sprite.image(lastUpdateTime);
                final AffineTransform transform = new AffineTransform();
                transform.translate(x, (double) offset.y() + y);
                transform.scale(options.scale(), options.scale());
                graphics.drawImage(image, transform, null);
                final double distanceX = (double) ((sprite.width() + options.padding()) * options.scale());
                x += distanceX;
            }
            y += options.font().height() * options.scale() + options.lineSpacing();
        }
        resetOpacityConfig(options.opacity());
    }

    @Override
    public void drawSpriteBatch(final SpriteBatch spriteBatch, final ScreenBounds clip) {
        for (final var entry : spriteBatch.entriesInOrder()) {
            drawSprite(entry.sprite(), entry.offset(), entry.options(), clip);
        }
    }

    private void applyClip(final ScreenBounds clip) {
        if (!clip.equals(lastUsedClip)) {
            graphics.setClip(clip.offset().x(), clip.offset().y(), clip.width(), clip.height());
            lastUsedClip = clip;
        }
    }
}