package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;
import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtFont;
import static de.suzufa.screwbox.core.graphics.window.WindowText.text;
import static java.lang.Math.round;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.window.WindowCircle;
import de.suzufa.screwbox.core.graphics.window.WindowFill;
import de.suzufa.screwbox.core.graphics.window.WindowLine;
import de.suzufa.screwbox.core.graphics.window.WindowPolygon;
import de.suzufa.screwbox.core.graphics.window.WindowRectangle;
import de.suzufa.screwbox.core.graphics.window.WindowRepeatingSprite;
import de.suzufa.screwbox.core.graphics.window.WindowSprite;
import de.suzufa.screwbox.core.graphics.window.WindowText;
import de.suzufa.screwbox.core.loop.Metrics;

public class DefaultRenderer implements Renderer {

    private final Frame frame;
    private final Metrics metrics;
    private Graphics2D graphics;

    public DefaultRenderer(final Frame frame, final Metrics metrics) {
        this.metrics = metrics;
        this.frame = frame;
        this.frame.setIgnoreRepaint(true);
        graphics = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        initializeFontDrawing();
    }

    private void initializeFontDrawing() {
        draw(text(Offset.origin(), "-", new Font("Futura", 1), Color.WHITE));
    }

    @Override
    public void updateScreen(final boolean antialiased) {
        frame.getBufferStrategy().show();
        graphics.dispose();
        graphics = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        if (antialiased) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        graphics.setColor(toAwtColor(Color.BLACK));
        graphics.fillRect(0, 0, frame.getWidth(), frame.getHeight());
    }

    @Override
    public void draw(final WindowRectangle rectangle) {
        graphics.setColor(toAwtColor(rectangle.color()));
        final WindowBounds bounds = rectangle.bounds();
        graphics.fillRect(
                bounds.offset().x(),
                bounds.offset().y(),
                bounds.dimension().width(),
                bounds.dimension().height());
    }

    @Override
    public void draw(WindowFill fill) {
        graphics.setColor(toAwtColor(fill.color()));
        graphics.fillRect(0, 0, frame.getWidth(), frame.getHeight());
    }

    @Override
    public Sprite takeScreenshot() {
        try {
            final Rectangle rectangle = new Rectangle(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
            final BufferedImage screenCapture = new Robot().createScreenCapture(rectangle);
            return Sprite.fromImage(screenCapture);
        } catch (final AWTException e) {
            throw new IllegalStateException("failed to take screenshot", e);
        }

    }

    @Override
    public void draw(final WindowCircle circle) {
        graphics.setColor(toAwtColor(circle.color()));
        final int x = circle.offset().x() - circle.diameter() / 2;
        final int y = circle.offset().y() - circle.diameter() / 2;
        graphics.fillOval(x, y, circle.diameter(), circle.diameter());
    }

    @Override
    public void draw(final WindowPolygon polygon) {
        graphics.setColor(toAwtColor(polygon.color()));
        final Polygon awtPolygon = new Polygon();
        for (final var point : polygon.points()) {
            awtPolygon.addPoint(point.x(), point.y());
        }
        graphics.fillPolygon(awtPolygon);
    }

    @Override
    public int calculateTextWidth(final String text, final Font font) {
        return graphics.getFontMetrics(toAwtFont(font)).stringWidth(text);
    }

    private void applyOpacityConfig(final Percentage opacity) {
        if (!opacity.isMaxValue()) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity.valueFloat()));
        }
    }

    private void resetOpacityConfig(final Percentage opacity) {
        if (!opacity.isMaxValue()) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
    }

    @Override
    public void draw(final WindowText text) {
        graphics.setColor(toAwtColor(text.color()));
        graphics.setFont(toAwtFont(text.font()));

        final var offset = text.centered() ? calculateCenterOffset(text) : text.offset();
        graphics.drawString(text.text(), offset.x(), offset.y());

    }

    private Offset calculateCenterOffset(final WindowText text) {
        final int textWidth = calculateTextWidth(text.text(), text.font());
        return Offset.at(text.offset().x() - textWidth / 2.0, text.offset().y());
    }

    @Override
    public void draw(final WindowSprite sprite) {
        applyOpacityConfig(sprite.opacity());

        if (!sprite.rotation().isNone()) {
            final double x = sprite.offset().x() + sprite.sprite().dimension().width() * sprite.scale() / 2.0;
            final double y = sprite.offset().y() + sprite.sprite().dimension().height() * sprite.scale() / 2.0;
            final double radians = sprite.rotation().radians();
            graphics.rotate(radians, x, y);
            drawSpriteInContext(sprite);
            graphics.rotate(-radians, x, y);
        } else {
            drawSpriteInContext(sprite);
        }

        resetOpacityConfig(sprite.opacity());
    }

    private void drawSpriteInContext(final WindowSprite sprite) {
        final Image image = sprite.sprite().getImage(metrics.timeOfLastUpdate());
        final AffineTransform transform = new AffineTransform();
        transform.translate(sprite.offset().x(), sprite.offset().y());
        transform.scale(sprite.scale(), sprite.scale());
        graphics.drawImage(image, transform, frame);
    }

    @Override
    public void draw(final WindowRepeatingSprite repeatingSprite) {
        final long spriteWidth = round(repeatingSprite.sprite().dimension().width() * repeatingSprite.scale());
        final long spriteHeight = round(repeatingSprite.sprite().dimension().height() * repeatingSprite.scale());
        final long countX = frame.getWidth() / spriteWidth + 1;
        final long countY = frame.getHeight() / spriteHeight + 1;
        final double offsetX = repeatingSprite.offset().x() % spriteWidth;
        final double offsetY = repeatingSprite.offset().y() % spriteHeight;

        for (long x = 0; x <= countX; x++) {
            for (long y = 0; y <= countY; y++) {
                final Offset thisOffset = Offset.at(x * spriteWidth + offsetX, y * spriteHeight + offsetY);
                draw(WindowSprite.sprite(repeatingSprite.sprite(), thisOffset, repeatingSprite.scale(),
                        repeatingSprite.opacity()));
            }
        }
    }

    @Override
    public void draw(final WindowLine line) {
        graphics.setColor(toAwtColor(line.color()));
        graphics.drawLine(line.from().x(), line.from().y(), line.to().x(), line.to().y());
    }

}
