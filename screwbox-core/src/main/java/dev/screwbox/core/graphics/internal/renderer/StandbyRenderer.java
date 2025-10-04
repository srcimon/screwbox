package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.graphics.internal.Renderer;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class StandbyRenderer implements Renderer {

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        // does nothing
    }

    @Override
    public void fillWith(final Color color, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void rotate(final Angle rotation, final ScreenBounds clip, final Color backgroundColor) {
        // does nothing
    }

    @Override
    public void fillWith(Sprite sprite, SpriteFillOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawOval(final Offset offset, final int radiusX, final  int radiusY, final OvalDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawPolygon(final List<Offset> nodes, final PolygonDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }
}
