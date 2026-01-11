package dev.screwbox.playground;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.softphysics.ClothComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;

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

    /**
     * Optional texture that will be used for rendering. Will use {@link #color} when no texture is specified.
     */
    public Sprite texture;

    /**
     * Color that will be used for rendering, when no {@link #texture} is specified.
     */
    public Color color = Color.MAGENTA;

    /**
     * Range of brightness difference that will be applied at rendering.
     */
    public Percent brightnessRange = Percent.half();

    /**
     * Modify the {@link Color#brightness()} impact used for rendering. Higher values will lower the brightness differences
     * for the same
     */
    public double sizeImpactModifier = 1.5;
}
