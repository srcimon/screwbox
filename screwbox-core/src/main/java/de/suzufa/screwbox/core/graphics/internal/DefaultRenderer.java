package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;
import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtFont;

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
import java.util.List;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class DefaultRenderer implements Renderer {

    private final Frame frame;
    private Time lastUpdateTime = Time.now();
    private Graphics2D graphics;

    public DefaultRenderer(final Frame frame) {
        this.frame = frame;
        this.frame.setIgnoreRepaint(true);
        graphics = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        initializeFontDrawing();
    }

    private void initializeFontDrawing() {
        drawText(Offset.origin(), "-", new Font("Arial", 1), Color.WHITE);
    }

    @Override
    public void updateScreen(final boolean antialiased) {
        lastUpdateTime = Time.now();
        frame.getBufferStrategy().show();
        graphics.dispose();
        graphics = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();
        if (antialiased) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    @Override
    public void fillWith(final Color color) {
        graphics.setColor(toAwtColor(color));
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
    public void drawPolygon(final List<Offset> points, final Color color) {
        graphics.setColor(toAwtColor(color));
        final Polygon awtPolygon = new Polygon();
        for (final var point : points) {
            awtPolygon.addPoint(point.x(), point.y());
        }
        graphics.fillPolygon(awtPolygon);
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
    public void drawText(final Offset offset, final String text, final Font font, final Color color) {
        graphics.setColor(toAwtColor(color));
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
            final Rotation rotation) {
        applyOpacityConfig(opacity);

        if (!rotation.isNone()) {
            final double x = origin.x() + sprite.dimension().width() * scale / 2.0;
            final double y = origin.y() + sprite.dimension().height() * scale / 2.0;
            final double radians = rotation.radians();
            graphics.rotate(radians, x, y);
            drawSpriteInContext(sprite, origin, scale);
            graphics.rotate(-radians, x, y);
        } else {
            drawSpriteInContext(sprite, origin, scale);
        }

        resetOpacityConfig(opacity);
    }

    private void drawSpriteInContext(final Sprite sprite, final Offset origin, final double scale) {
        final Image image = sprite.getImage(lastUpdateTime);
        final AffineTransform transform = new AffineTransform();
        transform.translate(origin.x(), origin.y());
        transform.scale(scale, scale);
        graphics.drawImage(image, transform, frame);
    }

    @Override
    public void drawRectangle(final WindowBounds bounds, final Color color) {
        graphics.setColor(toAwtColor(color));
        graphics.fillRect(
                bounds.offset().x(),
                bounds.offset().y(),
                bounds.dimension().width(),
                bounds.dimension().height());
    }

    @Override
    public void drawCircle(final Offset offset, final int diameter, final Color color) {
        graphics.setColor(toAwtColor(color));
        final int x = offset.x() - diameter / 2;
        final int y = offset.y() - diameter / 2;
        graphics.fillOval(x, y, diameter, diameter);
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final Color color) {
        graphics.setColor(toAwtColor(color));
        graphics.drawLine(from.x(), from.y(), to.x(), to.y());
    }

}
