package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.graphics.internal.Renderer;
import dev.screwbox.core.utils.Latch;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class StandbyProxyRenderer implements Renderer {

    private final Latch<Renderer> renderer;
    private int framesToSkip = 0;

    public StandbyProxyRenderer(final Renderer renderer) {
        this.renderer = Latch.of(new StandbyRenderer(), renderer);
    }

    public void toggleOnOff() {
        this.renderer.toggle();
    }

    /**
     * Can be uses to avoid graphic glitches when changing graphics configuration while async rendering is in progress.
     * E.g. toggling split screen mode.
     */
    public void skipFrames() {
        framesToSkip = 2;
    }

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        if (framesToSkip > 0) {
            framesToSkip--;
        } else {
            renderer.active().updateContext(graphics);
        }
    }

    @Override
    public void fillWith(final Color color, final ScreenBounds clip) {
        renderer.active().fillWith(color, clip);
    }

    @Override
    public void rotate(final Angle rotation, final ScreenBounds clip, final Color backgroundColor) {
        renderer.active().rotate(rotation, clip, backgroundColor);
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options, final ScreenBounds clip) {
        renderer.active().fillWith(sprite, options, clip);
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawText(offset, text, options, clip);
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawRectangle(offset, size, options, clip);
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawLine(from, to, options, clip);
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawCircle(offset, radius, options, clip);
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawSprite(sprite, origin, options, clip);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawSprite(sprite, origin, options, clip);
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawText(offset, text, options, clip);
    }

    @Override
    public void drawPolygon(final List<Offset> nodes, final PolygonDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawPolygon(nodes, options, clip);
    }
}
