package de.suzufa.screwbox.core.graphics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public interface Renderer {

    void updateScreen(boolean antialiased);

    Sprite takeScreenshot();

    void fillWith(Color color);

    void drawRectangle(WindowBounds bounds, Color color);

    void drawCircle(Offset offset, int diameter, Color color);

    void drawSprite(Sprite sprite, Offset origin, double scale, Percentage opacity, Rotation rotation);

    void drawText(Offset offset, String text, Font font, Color color);

    void drawLine(Offset from, Offset to, Color color);

    void drawPolygon(List<Offset> points, Color color);

    void drawTextCentered(Offset position, String text, Font font, Color color);

}
