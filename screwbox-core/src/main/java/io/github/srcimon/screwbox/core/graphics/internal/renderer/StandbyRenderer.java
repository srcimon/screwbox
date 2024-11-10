package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Rotation;
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

import java.awt.*;
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
    public void rotate(final Rotation rotation, final ScreenBounds clip, final Color backgroundColor) {
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
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options, final ScreenBounds clip) {
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
    public void drawText(Offset offset, String text, TextDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawSpriteBatch(SpriteBatch spriteBatch, final ScreenBounds clip) {
        // does nothing
    }

}
