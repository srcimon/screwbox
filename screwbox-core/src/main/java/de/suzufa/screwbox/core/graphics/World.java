package de.suzufa.screwbox.core.graphics;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.graphics.world.WorldLine;
import de.suzufa.screwbox.core.graphics.world.WorldPolygon;
import de.suzufa.screwbox.core.graphics.world.WorldRectangle;
import de.suzufa.screwbox.core.graphics.world.WorldSprite;
import de.suzufa.screwbox.core.graphics.world.WorldText;

public interface World {

    void draw(WorldRectangle rectangle);

    void draw(WorldSprite sprite);

    void draw(WorldText text);

    void draw(WorldLine line);
    
    void draw(WorldPolygon polygon);

    Bounds visibleArea();

}