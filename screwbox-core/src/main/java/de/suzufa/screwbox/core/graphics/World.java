package de.suzufa.screwbox.core.graphics;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.world.WorldLine;
import de.suzufa.screwbox.core.graphics.world.WorldPolygon;
import de.suzufa.screwbox.core.graphics.world.WorldRectangle;

public interface World {

    World setDrawingColor(Color color);

    Color drawingColor();

    void draw(WorldRectangle rectangle);

    World drawSprite(Sprite sprite, Vector origin, double scale, Percentage opacity, Rotation rotation);

    default World drawSprite(final Sprite sprite, final Vector origin, final Percentage opacity) {
        return drawSprite(sprite, origin, 1, opacity, Rotation.none());
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

    void draw(WorldLine line);

    void draw(WorldPolygon polygon);

    Bounds visibleArea();

}