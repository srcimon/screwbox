package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;

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

    Window drawSprite(Sprite sprite, Offset origin, double scale, Percentage opacity, Rotation rotation);

    default Window drawSprite(final Sprite sprite, final Offset origin, final Percentage opacity) {
        return drawSprite(sprite, origin, 1, opacity, Rotation.none());
    }

    default Window drawSprite(final Sprite sprite, final Offset origin) {
        return drawSprite(sprite, origin, Percentage.max());
    }

    Window drawText(Offset offset, String text, Font font, Color color);

    default Window drawText(final Offset offset, final String text, final Font font) {
        return drawText(offset, text, font, drawingColor());
    }

    default Window drawTextCentered(final Offset offset, final String text, final Font font, final Color color) {
        final int textWidth = calculateTextWidth(text, font);
        final var centerOffset = Offset.at(offset.x() - textWidth / 2.0, offset.y());
        return drawText(centerOffset, text, font, color);
    }

    default Window drawTextCentered(final Offset offset, final String text, final Font font) {
        return drawTextCentered(offset, text, font, drawingColor());
    }

    Window fillWith(Offset offset, Sprite sprite, double scale, Percentage opacity);

    default Window fillWith(final Sprite sprite, final double scale) {
        return fillWith(Offset.origin(), sprite, scale, Percentage.max());
    }

    default Window fillWith(final Offset offset, final Sprite sprite, final double scale) {
        return fillWith(offset, sprite, scale, Percentage.max());
    }

    default Window fillWith(final Offset offset, final Sprite sprite) {
        return fillWith(offset, sprite, 1);
    }

    Window drawLine(Offset from, Offset to, Color color);

    default Window drawLine(final Offset from, final Offset to) {
        return drawLine(from, to, drawingColor());
    }

    Window drawPolygon(List<Offset> points, Color color);

    default Window drawPolygon(final List<Offset> points) {
        return drawPolygon(points, drawingColor());
    }

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
