package de.suzufa.screwbox.core.graphics;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.world.WorldLine;
import de.suzufa.screwbox.core.graphics.world.WorldPolygon;
import de.suzufa.screwbox.core.graphics.world.WorldRectangle;
import de.suzufa.screwbox.core.graphics.world.WorldText;

public interface World {

    // TODO: Window setDrawingColor(Color color);

    // TODO: Color drawingColor();

    void draw(WorldRectangle rectangle);

    World drawSprite(Sprite sprite, Vector origin, double scale, Percentage opacity, Rotation rotation);

    default World drawSprite(final Sprite sprite, final Vector origin, final Percentage opacity) {
        return drawSprite(sprite, origin, 1, opacity, Rotation.none());
    }

    default World drawSprite(final Sprite sprite, final Vector origin) {
        return drawSprite(sprite, origin, Percentage.max());
    }

    void draw(WorldText text);

    void draw(WorldLine line);

    void draw(WorldPolygon polygon);

    Bounds visibleArea();

}