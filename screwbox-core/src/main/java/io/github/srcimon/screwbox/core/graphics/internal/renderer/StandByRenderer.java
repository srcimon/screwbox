package io.github.srcimon.screwbox.core.graphics.internal.renderer;

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
//TODO add finals
public class StandByRenderer implements Renderer {

    @Override
    public void updateContext(Supplier<Graphics2D> graphics) {
        // does nothing
    }

    @Override
    public void fillWith(Color color, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void fillWith(Sprite sprite, SpriteFillOptions options, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawText(Offset offset, String text, SystemTextDrawOptions options, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawRectangle(Offset offset, Size size, RectangleDrawOptions options, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawLine(Offset from, Offset to, LineDrawOptions options, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawCircle(Offset offset, int radius, CircleDrawOptions options, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawText(Offset offset, String text, TextDrawOptions options, ScreenBounds clip) {
        // does nothing
    }

    @Override
    public void drawSpriteBatch(SpriteBatch spriteBatch, ScreenBounds clip) {
        // does nothing
    }
}
