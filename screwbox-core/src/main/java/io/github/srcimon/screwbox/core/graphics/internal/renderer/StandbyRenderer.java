package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.awt.*;
import java.util.function.Supplier;

public class StandbyRenderer implements Renderer {

    @Override
    public void updateGraphicsContext(final Supplier<Graphics2D> graphicsSupplier, final Size canvasSize) {
        // does nothing
    }

    @Override
    public void fillWith(final Color color) {
        // does nothing
    }

    @Override
    public void fillWith(Sprite sprite, SpriteFillOptions options) {
        // does nothing
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        // does nothing
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        // does nothing
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        // does nothing
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        // does nothing
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        // does nothing
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        // does nothing
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawText(Offset offset, String text, TextDrawOptions options) {
        // does nothing
    }

    @Override
    public void drawSpriteBatch(SpriteBatch spriteBatch) {
        // does nothing
    }

}
