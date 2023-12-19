package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.*;

import java.util.function.Supplier;

public interface Renderer {

    void updateScreen(boolean antialiased);

    Sprite takeScreenshot();

    void fillWith(Color color);

    void fillRectangle(ScreenBounds bounds, Color color);

    void fillCircle(Offset offset, int diameter, Color color);

    void drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity, Rotation rotation,
                    Flip flip, ScreenBounds clipArea);

    void drawSprite(Supplier<Sprite> sprite, Offset origin, double scale, Percent opacity, Rotation rotation, Flip flip,
                    ScreenBounds clipArea);

    void drawText(Offset offset, String text, Font font, Color color);

    void drawLine(Offset from, Offset to, Color color);

    void drawTextCentered(Offset position, String text, Font font, Color color);

    void drawFadingCircle(Offset offset, int diameter, Color color);

    void drawCircle(Offset offset, int diameter, Color color, int strokeWidth);

    void drawRectangle(Offset offset, Size size, Rotation rotation, Color color);
}
