package de.suzufa.screwbox.core.graphics.internal;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.window.WindowLine;
import de.suzufa.screwbox.core.graphics.window.WindowPolygon;
import de.suzufa.screwbox.core.graphics.window.WindowRepeatingSprite;

public interface Renderer {

    void updateScreen(boolean antialiased);

    Sprite takeScreenshot();

    int calculateTextWidth(String text, Font font);

    void draw(WindowRepeatingSprite repeatingSprite);

    void draw(WindowLine line);

    void draw(WindowPolygon polygon);

    void fillWith(Color color);

    void drawRectangle(WindowBounds bounds, Color color);

    void drawCircle(Offset offset, int diameter, Color color);

    void drawSprite(Sprite sprite, Offset origin, double scale, Percentage opacity, Rotation rotation);

    void drawText(Offset offset, String text, Font font, Color color);
}
