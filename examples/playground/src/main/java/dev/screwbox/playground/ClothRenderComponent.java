package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Sprite;

public class ClothRenderComponent implements Component {

    /**
     * Draw order used for rendering.
     */
    public int drawOrder = 0;

    public boolean detailed = true;

    //TODO implement
    public Sprite texture;
}
