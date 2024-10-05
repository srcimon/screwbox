package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.*;

import java.awt.*;
import java.util.function.Supplier;

public interface Renderer {

    void updateGraphicsContext(Supplier<Graphics2D> graphicsSupplier, Size canvasSize, Rotation rotation);

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
