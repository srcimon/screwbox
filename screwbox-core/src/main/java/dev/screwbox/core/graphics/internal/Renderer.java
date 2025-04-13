package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Rotation;
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

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public interface Renderer {

    void updateContext(Supplier<Graphics2D> graphics);

    void rotate(Rotation rotation, ScreenBounds clip, Color backgroundColor);

    void fillWith(Color color, ScreenBounds clip);

    void fillWith(Sprite sprite, SpriteFillOptions options, ScreenBounds clip);

    void drawText(Offset offset, String text, SystemTextDrawOptions options, ScreenBounds clip);

    void drawRectangle(Offset offset, Size size, RectangleDrawOptions options, ScreenBounds clip);

    void drawLine(Offset from, Offset to, LineDrawOptions options, ScreenBounds clip);

    void drawCircle(Offset offset, int radius, CircleDrawOptions options, ScreenBounds clip);

    void drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip);

    void drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip);

    void drawText(Offset offset, String text, TextDrawOptions options, ScreenBounds clip);

    void drawPolygon(List<Offset> nodes, PolygonDrawOptions options, final ScreenBounds clip);
}
