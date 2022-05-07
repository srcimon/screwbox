package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public interface World {

    World setDrawingColor(Color color);

    Color drawingColor();

    World drawSprite(Sprite sprite, Vector origin, double scale, Percentage opacity, Rotation rotation);

    default World drawSprite(final Sprite sprite, final Vector origin, final Percentage opacity, Rotation rotation) {
        return drawSprite(sprite, origin, 1, opacity, rotation);
    }

    default World drawSprite(final Sprite sprite, final Vector origin, final Percentage opacity) {
        return drawSprite(sprite, origin, opacity, Rotation.none());
    }

    default World drawSprite(final Sprite sprite, final Vector origin) {
        return drawSprite(sprite, origin, Percentage.max());
    }

    World drawText(Vector offset, String text, Font font, Color color);

    default World drawText(final Vector offset, final String text, final Font font) {
        return drawText(offset, text, font, drawingColor());
    }

    World drawTextCentered(Vector position, String text, Font font, Color color);

    default World drawTextCentered(final Vector position, final String text, final Font font) {
        return drawTextCentered(position, text, font, drawingColor());
    }

    World drawLine(Vector from, Vector to, Color color);

    default World drawLine(final Segment line, final Color color) {
        return drawLine(line.from(), line.to(), color);
    }

    default World drawLine(final Segment line) {
        return drawLine(line, drawingColor());
    }

    default World drawLine(final Vector from, final Vector to) {
        return drawLine(from, to, drawingColor());
    }

    World drawPolygon(List<Vector> points, Color color);

    default World drawPolygon(List<Vector> points) {
        return drawPolygon(points, drawingColor());
    }

    World drawRectangle(Bounds bounds, Color color);

    default World drawRectangle(Bounds bounds) {
        return drawRectangle(bounds, drawingColor());
    }

    Bounds visibleArea();

}