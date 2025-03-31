package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;
import io.github.srcimon.screwbox.core.graphics.options.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.PolygonDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.options.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.TextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import io.github.srcimon.screwbox.core.graphics.internal.ShaderResolver;
import io.github.srcimon.screwbox.core.utils.TextUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.graphics.internal.AwtMapper.toAwtColor;
import static java.util.Objects.nonNull;

public class DefaultRenderer implements Renderer {

    private static final float[] FADEOUT_FRACTIONS = new float[]{0.0f, 0.3f, 0.6f, 1f};
    private static final java.awt.Color FADEOUT_COLOR = toAwtColor(Color.TRANSPARENT);

    private Time time = Time.now();
    private Graphics2D graphics;
    private Color lastUsedColor;
    private ScreenBounds lastUsedClip;
    private ShaderSetup defaultShader = null;

    public void setDefaultShader(final ShaderSetup defaultShader) {
        this.defaultShader = defaultShader;
    }

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        time = Time.now();
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
                drawImageUsingShaderSetup(sprite, options.shaderSetup(), transform, false);
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
        final int width = sprite.size().width();
        final int height = sprite.size().height();
        final double xCorrect = options.isFlipHorizontal() ? options.scale() * width : 0;
        final double yCorrect = options.isFlipVertical() ? options.scale() * height : 0;

        final AffineTransform transform = new AffineTransform();
        if (options.spin().isZero()) {
            transform.translate(origin.x() + xCorrect, origin.y() + yCorrect);
        } else {
            double distort = Ease.SINE_IN_OUT.applyOn(options.spin()).value() * -2 + 1;
            if (options.isSpinHorizontal()) {
                transform.translate(origin.x() + options.scale() * width / 2.0, origin.y());
                transform.scale(distort, 1);
                transform.translate(options.scale() * width / -2.0 + xCorrect, yCorrect);
            } else {
                transform.translate(origin.x(), origin.y() + options.scale() * height / 2.0);
                transform.scale(1, distort);
                transform.translate(xCorrect, options.scale() * height / -2.0 + yCorrect);
            }
        }

        transform.scale(options.scale() * (options.isFlipHorizontal() ? -1 : 1), options.scale() * (options.isFlipVertical() ? -1 : 1));
        drawImageUsingShaderSetup(sprite, options.shaderSetup(), transform, options.isIgnoreOverlayShader());
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
            for (final var sprite : allSprites) {
                final AffineTransform transform = new AffineTransform();
                transform.translate(x, (double) offset.y() + y);
                transform.scale(options.scale(), options.scale());
                ShaderSetup shaderSetup = options.shaderSetup();
                drawImageUsingShaderSetup(sprite, shaderSetup, transform, false);
                final double distanceX = (sprite.width() + options.padding()) * options.scale();
                x += distanceX;
            }
            y += (int) (1.0 * options.font().height() * options.scale() + options.lineSpacing());
        }
        resetOpacityConfig(options.opacity());
    }

    @Override
    public void drawPolygon(final List<Offset> nodes, final PolygonDrawOptions options) {
        final int[] xValues = new int[nodes.size()];
        final int[] yValues = new int[nodes.size()];

        int index = 0;
        for (var offset : nodes) {
            xValues[index] = offset.x();
            yValues[index] = offset.y();
            index++;
        }

        Polygon polygon = new Polygon(xValues, yValues, nodes.size());
        switch (options.style()) {
            case OUTLINE -> {
                applyNewColor(options.color());
                final var oldStroke = graphics.getStroke();
                graphics.setStroke(new BasicStroke(options.strokeWidth()));
                graphics.drawPolygon(polygon);
                graphics.setStroke(oldStroke);
            } case FILLED  ->  {
                applyNewColor(options.color());
                graphics.fillPolygon(polygon);
            }
            case VERTICAL_GRADIENT -> {
                int minY = Integer.MAX_VALUE;
                int maxY = Integer.MIN_VALUE;
                for(int y : yValues) {
                    if(y < minY) {
                        minY = y;
                    }
                    if(y> maxY) {
                        maxY = y;
                    }
                }
                graphics.setPaint(new GradientPaint(0,minY, AwtMapper.toAwtColor(options.color()), 0, maxY, AwtMapper.toAwtColor(options.secondaryColor())));
                graphics.fillPolygon(polygon);
            }
        }
    }

    private void applyClip(final ScreenBounds clip) {
        if (!clip.equals(lastUsedClip)) {
            graphics.setClip(clip.offset().x(), clip.offset().y(), clip.width(), clip.height());
            lastUsedClip = clip;
        }
    }

    private void drawImageUsingShaderSetup(final Sprite sprite, final ShaderSetup shaderSetup, final AffineTransform transform, final boolean ignoreOverlay) {
        final var appliedShader = ShaderResolver.resolveShader(defaultShader, shaderSetup, ignoreOverlay);
        final Image image = sprite.image(appliedShader, time);

        // react on shader induced size change
        if (nonNull(appliedShader)) {
            int deltaX = image.getWidth(null) - sprite.width();
            int deltaY = image.getHeight(null) - sprite.height();
            if (deltaX != 0 || deltaY != 0) {
                transform.translate(-deltaX / 2.0, -deltaY / 2.0);
            }
        }
        graphics.drawImage(image, transform, null);
    }

}