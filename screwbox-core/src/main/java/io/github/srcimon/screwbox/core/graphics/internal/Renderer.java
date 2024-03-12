package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.*;

import java.awt.*;
import java.util.function.Supplier;

public interface Renderer {

    void updateGraphicsContext(Supplier<Graphics2D> graphicsSupplier, final Size canvasSize);

    void fillWith(Color color);

    void drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity, Rotation rotation,
                    Flip flip, ScreenBounds clip);

    void drawSprite(Supplier<Sprite> sprite, Offset origin, double scale, Percent opacity, Rotation rotation, Flip flip,
                    ScreenBounds clip);

    void drawText(Offset offset, String text, Font font, Color color);

    void drawTextCentered(Offset position, String text, Font font, Color color);

    void drawRectangle(Offset offset, Size size, RectangleDrawOptions options);

    void drawLine(Offset from, Offset to, LineDrawOptions options);

    void drawCircle(Offset offset, int radius, CircleDrawOptions options);

    void drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options);
}
