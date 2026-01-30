package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.Renderer;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

import static dev.screwbox.core.graphics.options.SystemTextDrawOptions.Alignment.LEFT;
import static dev.screwbox.core.graphics.options.SystemTextDrawOptions.Alignment.RIGHT;

/**
 * Prevent unnecessary and invalid rendering tasks hitting the actual render.
 */
public class FirewallRenderer implements Renderer {

    private final Renderer next;

    public FirewallRenderer(final Renderer next) {
        this.next = next;
    }

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        next.updateContext(graphics);
    }

    @Override
    public void fillWith(final Color color, final ScreenBounds clip) {
        if (color.isVisible()) {
            next.fillWith(color, clip);
        }
    }

    @Override
    public void rotate(final Angle rotation, final ScreenBounds clip, final Color backgroundColor) {
        if (!rotation.isZero()) {
            next.rotate(rotation, clip, backgroundColor);
        }
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options, final ScreenBounds clip) {
        if (!options.opacity().isZero()) {
            next.fillWith(sprite, options, clip);
        }
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        if (options.color().isVisible() && !text.isEmpty()
            && !(RIGHT.equals(options.alignment()) && offset.x() < 0)
            && !(LEFT.equals(options.alignment()) && offset.x() > clip.width())) {
            next.drawText(offset, text, options, clip);
        }
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        final var rectangleBounds = options.rotation().isZero()
            ? new ScreenBounds(offset, size)
            : new ScreenBounds(offset, size).expand(Math.max(size.width(), size.height()) / 2);
        if (options.color().isVisible() && size.isValid() && clip.intersects(rectangleBounds)) {
            next.drawRectangle(offset, size, options, clip);
        }
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        final int minX = Math.min(from.x(), to.x());
        final int minY = Math.min(from.y(), to.y());
        final var size = Size.definedBy(from, to);
        final var screenBounds = new ScreenBounds(Offset.at(minX, minY), size);
        if (options.color().isVisible() && screenBounds.intersects(clip)) {
            next.drawLine(from, to, options, clip);
        }
    }

    @Override
    public void drawOval(final Offset offset, final int radiusX, final int radiusY, final OvalDrawOptions options, final ScreenBounds clip) {
        final var circleBounds = new ScreenBounds(offset.x() - radiusX, offset.y() - radiusY, radiusX * 2, radiusY * 2);
        if (options.color().isVisible() && radiusX > 0 && radiusY > 0 && circleBounds.intersects(clip)
            && (!OvalDrawOptions.Style.FADING.equals(options.style()) || options.color().opacity().value() > 0.01)) {
            next.drawOval(offset, radiusX, radiusY, options, clip);
        }
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        if (!options.opacity().isZero()) {
            next.drawSprite(sprite, origin, options, clip);
        }
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        if (!options.opacity().isZero()) {
            next.drawSprite(sprite, origin, options, clip);
        }
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        if (!options.opacity().isZero() && !text.isEmpty() && options.scale() > 0) {
            next.drawText(offset, text, options, clip);
        }
    }

    @Override
    public void drawPolygon(final List<Offset> nodes, final PolygonDrawOptions options, final ScreenBounds clip) {
        if (!nodes.isEmpty() && options.color().isVisible() ) {
            next.drawPolygon(nodes, options, clip);
        }
    }
}
