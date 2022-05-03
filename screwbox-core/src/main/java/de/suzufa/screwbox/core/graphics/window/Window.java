package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public interface Window {

    Window drawRectangle(WindowBounds bounds, Color color);

    default Window drawRectangle(Offset origin, Dimension size, Color color) {
        return drawRectangle(new WindowBounds(origin, size), color);
    }

    Window draw(WindowText text);

    Window draw(WindowSprite sprite);

    Window draw(WindowRepeatingSprite repeatingSprite);

    Window draw(WindowLine line);

    Window draw(WindowCircle circle);

    Window draw(WindowPolygon polygon);

    Window fillWithColor(Color color);

    int calculateTextWidth(String text, Font font);

    Sprite takeScreenshot();

    Offset center();

    Dimension size();

    Offset position();

    Window open();

    Window close();

    Window setTitle(String title);

    Window moveTo(Offset position);

    boolean isVisible(WindowBounds bounds);

}
