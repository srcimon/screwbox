package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;

/**
 * Adds rendering of soft bodies with a {@link ClothComponent}.
 *
 * @since 3.20.0
 */
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
    //TODO changelog
    public Color color = Color.RED;

    /**
     * Range of brightness difference that will be applied at rendering.
     */
    public Percent brightnessRange = Percent.half();

    /**
     * Modify the {@link Color#brightness()} impact of mesh cell size changes. The mesh rendering process uses difference
     * in the cell sizes in comparison to the {@link ClothComponent#meshCellSize} to calculate the applied color brightness.
     */
    public Percent sizeImpactModifier = Percent.quarter();
}
