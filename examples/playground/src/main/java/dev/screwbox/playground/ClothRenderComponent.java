package dev.screwbox.playground;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.softphysics.ClothComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;

/**
 * Adds rendering of soft bodies with a {@link ClothComponent}.
 */
//TODO document since
public class ClothRenderComponent implements Component {

    /**
     * Draw order used for rendering.
     */
    public int drawOrder = 0;

    /**
     * Configure more detailed rendering. Will render two triangles when enabled or one tetragon when disabled for
     * every cloth mesh cell.
     */
    public boolean detailed = true;

    //TODO implement
    public Sprite texture = SpriteBundle.MARKER_SKULL.get();
    public Color color = Color.ORANGE;
    public Percent brightnessRange = Percent.half();
}
