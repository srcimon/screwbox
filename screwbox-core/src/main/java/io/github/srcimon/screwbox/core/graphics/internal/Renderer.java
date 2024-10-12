package io.github.srcimon.screwbox.core.graphics.internal;

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

    void updateClip(ScreenBounds clip);

    void fillWith(Color color);

    void fillWith(Sprite sprite, SpriteFillOptions options);

    void drawText(Offset offset, String text, SystemTextDrawOptions options);

    void drawRectangle(Offset offset, Size size, RectangleDrawOptions options);

    void drawLine(Offset from, Offset to, LineDrawOptions options);

    void drawCircle(Offset offset, int radius, CircleDrawOptions options);

    void drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options);

    void drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options);

    void drawText(Offset offset, String text, TextDrawOptions options);

    void drawSpriteBatch(SpriteBatch spriteBatch);
}
