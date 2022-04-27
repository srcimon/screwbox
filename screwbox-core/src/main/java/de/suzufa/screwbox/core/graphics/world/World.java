package de.suzufa.screwbox.core.graphics.world;

import de.suzufa.screwbox.core.Bounds;

public interface World {

    void draw(WorldRectangle rectangle);

    void draw(WorldSprite sprite);

    void draw(WorldText text);

    void draw(WorldLine line);
    
    void draw(WorldPolygon polygon);

    Bounds visibleArea();

}