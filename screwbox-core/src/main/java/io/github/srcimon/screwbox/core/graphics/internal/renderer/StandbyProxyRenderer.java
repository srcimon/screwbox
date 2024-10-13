package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
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
import io.github.srcimon.screwbox.core.utils.Latch;

import java.awt.*;
import java.util.function.Supplier;

public class StandbyProxyRenderer implements Renderer {

    private final Latch<Renderer> renderer;

    public StandbyProxyRenderer(final Renderer renderer) {
        this.renderer = Latch.of(new StandbyRenderer(), renderer);
    }

    public void toggle() {
        this.renderer.toggle();
    }

    @Override
    public void updateGraphicsContext(final Supplier<Graphics2D> graphicsSupplier, final Size canvasSize) {
        renderer.active().updateGraphicsContext(graphicsSupplier, canvasSize);
    }

    @Override
    public void fillWith(final Color color) {
        renderer.active().fillWith(color);
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options) {
        renderer.active().fillWith(sprite, options);
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        renderer.active().drawText(offset, text, options);
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        renderer.active().drawRectangle(offset, size, options);
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        renderer.active().drawLine(from, to, options);
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        renderer.active().drawCircle(offset, radius, options);
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.active().drawSprite(sprite, origin, options);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.active().drawSprite(sprite, origin, options);
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options) {
        renderer.active().drawText(offset, text, options);
    }

    @Override
    public void drawSpriteBatch(final SpriteBatch spriteBatch) {
        renderer.active().drawSpriteBatch(spriteBatch);
    }
}
