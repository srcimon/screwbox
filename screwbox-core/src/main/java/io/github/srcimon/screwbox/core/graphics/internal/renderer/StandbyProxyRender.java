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

import java.awt.*;
import java.util.function.Supplier;

public class StandbyProxyRender implements Renderer {

    private final Renderer next;

    private boolean isStandby = true;

    public StandbyProxyRender(final Renderer next) {
        this.next = next;
    }

    public void standBy() {
        isStandby = true;
    }

    public void wakeUp() {
        isStandby = false;
    }

    @Override
    public void updateGraphicsContext(final Supplier<Graphics2D> graphicsSupplier, final Size canvasSize) {
        if (!isStandby) {
            next.updateGraphicsContext(graphicsSupplier, canvasSize);
        }
    }

    @Override
    public void fillWith(final Color color) {
        if (!isStandby) {
            next.fillWith(color);
        }
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options) {
        if (!isStandby) {
            next.fillWith(sprite, options);
        }
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        if (!isStandby) {
            next.drawText(offset, text, options);
        }
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        if (!isStandby) {
            next.drawRectangle(offset, size, options);
        }
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        if (!isStandby) {
            next.drawLine(from, to, options);
        }
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        if (!isStandby) {
            next.drawCircle(offset, radius, options);
        }
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        if (!isStandby) {
            next.drawSprite(sprite, origin, options);
        }
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        if (!isStandby) {
            next.drawSprite(sprite, origin, options);
        }
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options) {
        if (!isStandby) {
            next.drawText(offset, text, options);
        }
    }

    @Override
    public void drawSpriteBatch(final SpriteBatch spriteBatch) {
        if (!isStandby) {
            next.drawSpriteBatch(spriteBatch);
        }
    }
}
