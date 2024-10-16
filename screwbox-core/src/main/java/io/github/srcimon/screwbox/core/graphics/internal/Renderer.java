package io.github.srcimon.screwbox.core.graphics.internal;

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

import java.awt.*;
import java.util.function.Supplier;

public interface Renderer {

    void updateContext(Supplier<Graphics2D> graphics);

    void rotate(Rotation rotation, ScreenBounds clip);

    void fillWith(Color color, ScreenBounds clip);

    void fillWith(Sprite sprite, SpriteFillOptions options, ScreenBounds clip);

    void drawText(Offset offset, String text, SystemTextDrawOptions options, ScreenBounds clip);

    void drawRectangle(Offset offset, Size size, RectangleDrawOptions options, ScreenBounds clip);

    void drawLine(Offset from, Offset to, LineDrawOptions options, ScreenBounds clip);

    void drawCircle(Offset offset, int radius, CircleDrawOptions options, ScreenBounds clip);

    void drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip);

    void drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip);

    void drawText(Offset offset, String text, TextDrawOptions options, ScreenBounds clip);

    void drawSpriteBatch(SpriteBatch spriteBatch, ScreenBounds clip);
}
