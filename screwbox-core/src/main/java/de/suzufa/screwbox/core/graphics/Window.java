package de.suzufa.screwbox.core.graphics;

import de.suzufa.screwbox.core.graphics.window.WindowLine;
import de.suzufa.screwbox.core.graphics.window.WindowPolygon;
import de.suzufa.screwbox.core.graphics.window.WindowRepeatingSprite;
import de.suzufa.screwbox.core.graphics.window.WindowSprite;
import de.suzufa.screwbox.core.graphics.window.WindowText;

public interface Window {

    Window setDrawingColor(Color color);

    Color drawingColor();

    Window drawRectangle(WindowBounds bounds, Color color);

    default Window drawRectangle(final WindowBounds bounds) {
        return drawRectangle(bounds, drawingColor());
    }

    default Window drawRectangle(final Offset origin, final Dimension size, final Color color) {
        return drawRectangle(new WindowBounds(origin, size), color);
    }

    default Window drawRectangle(final Offset origin, final Dimension size) {
        return drawRectangle(new WindowBounds(origin, size), drawingColor());
    }

    Window drawCircle(Offset offset, int diameter, Color color);

    default Window drawCircle(final Offset offset, final int diameter) {
        return drawCircle(offset, diameter, drawingColor());
    }

    Window draw(WindowText text);

    Window draw(WindowSprite sprite);

    Window draw(WindowRepeatingSprite repeatingSprite);

    Window draw(WindowLine line);

    Window draw(WindowPolygon polygon);

    Window fillWith(Color color);

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
