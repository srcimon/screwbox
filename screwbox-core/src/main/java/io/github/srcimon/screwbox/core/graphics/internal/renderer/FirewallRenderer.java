package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.options.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.PolygonDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.options.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.options.TextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.graphics.options.SystemTextDrawOptions.Alignment.LEFT;
import static io.github.srcimon.screwbox.core.graphics.options.SystemTextDrawOptions.Alignment.RIGHT;

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
        if (!color.opacity().isZero()) {
            next.fillWith(color, clip);
        }
    }

    @Override
    public void rotate(final Rotation rotation, final ScreenBounds clip, final Color backgroundColor) {
        if (!rotation.isNone()) {
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
        if (!options.color().opacity().isZero() && !text.isEmpty()
                && !(RIGHT.equals(options.alignment()) && offset.x() < 0)
                && !(LEFT.equals(options.alignment()) && offset.x() > clip.width())) {
            next.drawText(offset, text, options, clip);
        }
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        final var rectangleBounds = new ScreenBounds(offset, size);
        if (!options.color().opacity().isZero() && size.isValid() && clip.intersects(rectangleBounds)) {
            next.drawRectangle(offset, size, options, clip);
        }
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        final int minX = Math.min(from.x(), to.x());
        final int minY = Math.min(from.y(), to.y());
        final var size = Size.definedBy(from, to);
        final var screenBounds = new ScreenBounds(Offset.at(minX, minY), size);
        if (!options.color().opacity().isZero() && screenBounds.intersects(clip)) {
            next.drawLine(from, to, options, clip);
        }
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options, final ScreenBounds clip) {
        final var circleBounds = new ScreenBounds(offset.x() - radius, offset.y() - radius, radius * 2, radius * 2);
        if (!options.color().opacity().isZero() && radius > 0 && circleBounds.intersects(clip)) {
            next.drawCircle(offset, radius, options, clip);
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
    public void drawPolygon(final List<Offset> nodes, final PolygonDrawOptions options) {
        next.drawPolygon(nodes, options);
    }
}
