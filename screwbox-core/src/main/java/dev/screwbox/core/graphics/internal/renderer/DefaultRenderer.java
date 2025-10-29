package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.Renderer;
import dev.screwbox.core.graphics.internal.ShaderResolver;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.TextUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.function.Supplier;

import static dev.screwbox.core.graphics.internal.AwtMapper.toAwtColor;
import static java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE;
import static java.util.Objects.nonNull;

public class DefaultRenderer implements Renderer {

    private static final float[] FADEOUT_FRACTIONS = new float[]{0.0f, 0.3f, 0.6f, 1f};
    private static final java.awt.Color FADEOUT_COLOR = toAwtColor(Color.TRANSPARENT);

    private Time time = Time.now();
    private Graphics2D graphics;
    private ScreenBounds lastUsedClip;
    private ShaderSetup defaultShader = null;

    public void setDefaultShader(final ShaderSetup defaultShader) {
        this.defaultShader = defaultShader;
    }

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        time = Time.now();
        this.graphics = graphics.get();
        this.graphics.setColor(java.awt.Color.BLACK);
        lastUsedClip = null;
    }

    @Override
    public void fillWith(final Color color, final ScreenBounds clip) {
        applyClip(clip);
        graphics.setColor(toAwtColor(color));
        graphics.fillRect(clip.offset().x(), clip.offset().y(), clip.width(), clip.height());
    }

    @Override
    public void rotate(final Angle rotation, final ScreenBounds clip, final Color backgroundColor) {
        // not invoking fillWith(color) here to prevent setting clip
        graphics.setColor(toAwtColor(backgroundColor));
        graphics.fillRect(clip.x(), clip.y(), clip.width(), clip.height());
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
        graphics.setColor(toAwtColor(options.color()));
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

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        graphics.setColor(toAwtColor(options.color()));
        applyClip(clip);

        if (options.rotation().isZero()) {
            drawRectangleInContext(offset, size, options);
        } else {
            final double x = offset.x() + size.width() / 2.0;
            final double y = offset.y() + size.height() / 2.0;
            final double radians = options.rotation().radians();
            graphics.rotate(radians, x, y);
            drawRectangleInContext(offset, size, options);
            graphics.rotate(-radians, x, y);
        }
    }

    private void drawRectangleInContext(final Offset offset, final Size size, final RectangleDrawOptions options) {
        if (options.style() == RectangleDrawOptions.Style.FILLED || (options.style() == RectangleDrawOptions.Style.FADING && !options.isCurved())) {
            if (options.isCurved()) {
                graphics.fillRoundRect(offset.x(), offset.y(), size.width(), size.height(), options.curveRadius(), options.curveRadius());
            } else {
                graphics.fillRect(offset.x(), offset.y(), size.width(), size.height());
            }
        } else if (options.style() == RectangleDrawOptions.Style.FADING) {
            final var oldPaint = graphics.getPaint();
            final var radius = Math.min(options.curveRadius(), Math.min(size.width() / 2, size.height() / 2));
            final var safeSize = Size.of(Math.max(1, size.width() - 2 * radius), Math.max(1, size.height() - 2 * radius));
            final var innerBounds = new ScreenBounds(offset.add(radius, radius), safeSize);
            graphics.fillRect(innerBounds.offset().x(), innerBounds.offset().y(), innerBounds.width(), innerBounds.height());

            final var colors = buildFadeoutColors(options.color());

            graphics.setPaint(new LinearGradientPaint(innerBounds.x(), innerBounds.y(), innerBounds.x() - (float) radius, innerBounds.y(), FADEOUT_FRACTIONS, colors));
            graphics.fillRect(innerBounds.x() - radius, innerBounds.y(), radius, innerBounds.height());

            graphics.setPaint(new LinearGradientPaint(innerBounds.maxX(), innerBounds.y(), innerBounds.maxX() + (float) radius, innerBounds.y(), FADEOUT_FRACTIONS, colors));
            graphics.fillRect(innerBounds.maxX(), innerBounds.y(), radius, innerBounds.height());

            graphics.setPaint(new LinearGradientPaint(innerBounds.x(), innerBounds.y(), innerBounds.x(), innerBounds.y() - (float) radius, FADEOUT_FRACTIONS, colors));
            graphics.fillRect(innerBounds.x(), innerBounds.y() - radius, innerBounds.width(), radius);

            graphics.setPaint(new LinearGradientPaint(innerBounds.x(), innerBounds.maxY(), innerBounds.x(), innerBounds.maxY() + (float) radius, FADEOUT_FRACTIONS, colors));
            graphics.fillRect(innerBounds.x(), innerBounds.maxY(), innerBounds.width(), radius);

            final double doubleRadius = (double) radius + radius;
            final var topLeft = new Rectangle2D.Double((double) innerBounds.x() - radius, (double) innerBounds.y() - radius, doubleRadius, doubleRadius);
            if (!topLeft.isEmpty()) { // others will be empty as well
                graphics.setPaint(new RadialGradientPaint(topLeft, FADEOUT_FRACTIONS, colors, NO_CYCLE));
                graphics.fillRect(innerBounds.x() - radius, innerBounds.y() - radius, radius, radius);

                final var topRight = new Rectangle2D.Double((double) innerBounds.maxX() - radius, (double) innerBounds.y() - radius, doubleRadius, doubleRadius);
                graphics.setPaint(new RadialGradientPaint(topRight, FADEOUT_FRACTIONS, colors, NO_CYCLE));
                graphics.fillRect(innerBounds.maxX(), innerBounds.y() - radius, radius, radius);

                final var bottomLeft = new Rectangle2D.Double((double) innerBounds.x() - radius, (double) innerBounds.maxY() - radius, doubleRadius, doubleRadius);
                graphics.setPaint(new RadialGradientPaint(bottomLeft, FADEOUT_FRACTIONS, colors, NO_CYCLE));
                graphics.fillRect(innerBounds.x() - radius, innerBounds.maxY(), radius, radius);

                final var bottomRight = new Rectangle2D.Double((double) innerBounds.maxX() - radius, (double) innerBounds.maxY() - radius, doubleRadius, doubleRadius);
                graphics.setPaint(new RadialGradientPaint(bottomRight, FADEOUT_FRACTIONS, colors, NO_CYCLE));
                graphics.fillRect(innerBounds.maxX(), innerBounds.maxY(), radius, radius);
            }
            graphics.setPaint(oldPaint);
        } else {
            final var oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(options.strokeWidth()));
            if (options.isCurved()) {
                graphics.drawRoundRect(offset.x(), offset.y(), size.width(), size.height(), options.curveRadius(), options.curveRadius());
            } else {
                graphics.drawRect(offset.x(), offset.y(), size.width(), size.height());

            }
            graphics.setStroke(oldStroke);
        }
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        graphics.setColor(toAwtColor(options.color()));
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
    public void drawOval(final Offset offset, final int radiusX, final int radiusY, final OvalDrawOptions options, final ScreenBounds clip) {
        applyClip(clip);
        final int x = offset.x() - radiusX;
        final int y = offset.y() - radiusY;

        final int width = radiusX * 2;
        final int height = radiusY * 2;
        if (options.style() == OvalDrawOptions.Style.FILLED) {
            graphics.setColor(toAwtColor(options.color()));
            fillCircle(options, x, y, width, height);
        } else if (options.style() == OvalDrawOptions.Style.FADING) {
            final var oldPaint = graphics.getPaint();
            final Color color = options.color();
            final var colors = buildFadeoutColors(color);

            if (radiusX == radiusY) {
                graphics.setPaint(new RadialGradientPaint(
                        offset.x(),
                        offset.y(),
                        radiusX,
                        FADEOUT_FRACTIONS, colors));
            } else {
                final var gradientBounds = new Rectangle2D.Double((double) offset.x() - radiusX, (double) offset.y() - radiusY, width, height);
                graphics.setPaint(new RadialGradientPaint(gradientBounds, FADEOUT_FRACTIONS, colors, NO_CYCLE));
            }

            fillCircle(options, x, y, width, height);
            graphics.setPaint(oldPaint);
        } else {
            graphics.setColor(toAwtColor(options.color()));
            if (options.strokeWidth() == 1) {
                drawCircle(options, x, y, width, height);
            } else {
                var oldStroke = graphics.getStroke();
                graphics.setStroke(new BasicStroke(options.strokeWidth()));
                drawCircle(options, x, y, width, height);
                graphics.setStroke(oldStroke);
            }
        }
    }

    private void drawCircle(final OvalDrawOptions options, final int x, final int y, final int width, final int height) {
        if (options.arcAngle().isZero()) {
            graphics.drawOval(x, y, width, height);
        } else {
            graphics.drawArc(x, y, width, height, -(int) options.startAngle().degrees() + 90, -(int) options.arcAngle().degrees());
        }
    }

    private void fillCircle(final OvalDrawOptions options, final int x, final int y, final int width, final int height) {
        if (options.arcAngle().isZero()) {
            graphics.fillOval(x, y, width, height);
        } else {
            graphics.fillArc(x, y, width, height, -(int) options.startAngle().degrees() + 90, -(int) options.arcAngle().degrees());
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

        if (!options.rotation().isZero()) {
            final double x = origin.x() + sprite.width() * options.scale() / 2.0;
            final double y = origin.y() + sprite.height() * options.scale() / 2.0;
            final double radians = options.rotation().radians();
            final var origTransform = graphics.getTransform();
            final var rotatedTransform = graphics.getTransform();
            rotatedTransform.rotate(radians, x, y);
            graphics.setTransform(rotatedTransform);
            drawSpriteInContext(sprite, origin, options);
            graphics.setTransform(origTransform);
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
    public void drawPolygon(final List<Offset> nodes, final PolygonDrawOptions options, final ScreenBounds clip) {
        applyClip(clip);
        final var generalPath = createPolygonPath(nodes, options, clip);

        switch (options.style()) {
            case OUTLINE -> {
                graphics.setColor(toAwtColor(options.color()));
                final var oldStroke = graphics.getStroke();
                graphics.setStroke(new BasicStroke(options.strokeWidth()));
                graphics.draw(generalPath);
                graphics.setStroke(oldStroke);
            }
            case FILLED -> {
                graphics.setColor(toAwtColor(options.color()));
                graphics.fill(generalPath);
            }
            case VERTICAL_GRADIENT -> {
                int minY = Integer.MAX_VALUE;
                int maxY = Integer.MIN_VALUE;
                for (var node : nodes) {
                    if (node.y() < minY) {
                        minY = node.y();
                    }
                    if (node.y() > maxY) {
                        maxY = node.y();
                    }
                }
                var oldPaint = graphics.getPaint();
                graphics.setPaint(new GradientPaint(0, minY, toAwtColor(options.color()), 0, maxY, toAwtColor(options.secondaryColor())));
                graphics.fill(generalPath);
                graphics.setPaint(oldPaint);
            }
        }
    }

    private GeneralPath createPolygonPath(final List<Offset> nodes, final PolygonDrawOptions options, final ScreenBounds clip) {
        final var path = new GeneralPath();
        final Offset firstNode = nodes.getFirst().add(clip.offset());
        path.moveTo(firstNode.x(), firstNode.y());
        for (int i = 0; i < nodes.size(); i++) {
            final Offset node = nodes.get(i).add(clip.offset());
            switch (options.smoothing()) {
                case NONE -> path.lineTo(node.x(), node.y());
                case HORIZONTAL -> {
                    final boolean isEdge = i < 1 || i > nodes.size() - 1;
                    if (isEdge) {
                        path.lineTo(node.x(), node.y());
                    } else {
                        final Offset lastNode = nodes.get(i - 1).add(clip.offset());
                        final double halfXDistance = (node.x() - lastNode.x()) / 2.0;
                        path.curveTo(
                                lastNode.x() + halfXDistance, lastNode.y(), // bezier 1
                                node.x() - halfXDistance, node.y(), // bezier 2
                                node.x(), node.y()); // destination
                    }
                }
                case SPLINE -> {
                    boolean isCircular = nodes.getFirst().equals(nodes.getLast());
                    boolean isStart = i < nodes.size() - 1;
                    boolean isEnd = i >= nodes.size() - 2;
                    if(isStart || isCircular) {
                        Offset p0 = nodes.get(i).add(clip.offset());
                        Offset p1 = nodes.get((i + 1) % nodes.size()).add(clip.offset());

                        // Benachbarte Punkte für die Catmull-Rom-Formel
                        Offset p_prev = nodes.get((i - 1 + nodes.size()) % nodes.size()).add(clip.offset());
                        Offset p_next = nodes.get((i + 2) % nodes.size()).add(clip.offset());

                        // Kontrollpunkte berechnen (basierend auf Catmull-Rom)
                        double cp1x = isStart  ? p0.x() : p0.x() + (p1.x() - p_prev.x()) / 6.0;//TODO only when not connected
                        double cp1y =  isStart ? p0.y() : p0.y() + (p1.y() - p_prev.y()) / 6.0;//TODO only when not connected
                        double cp2x = isEnd ? p1.x() : p1.x() - (p_next.x() - p0.x()) / 6.0;//TODO only when not connected
                        double cp2y = isEnd ? p1.y() : p1.y() - (p_next.y() - p0.y()) / 6.0;//TODO only when not connected

                        // Fügen Sie das Bézier-Kurvensegment hinzu
                        path.curveTo(cp1x, cp1y, cp2x, cp2y, p1.x(), p1.y());
                    }
                }
            }
        }
        return path;
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

    private java.awt.Color[] buildFadeoutColors(final Color options) {
        return new java.awt.Color[]{
                toAwtColor(options),
                toAwtColor(options.opacity(options.opacity().value() / 2.0)),
                toAwtColor(options.opacity(options.opacity().value() / 4.0)),
                FADEOUT_COLOR};
    }
}