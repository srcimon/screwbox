package dev.screwbox.core.environment.sloshphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.io.Serial;

/**
 * Will render slosh volumes when added to entity also containing {@link SloshVolumeComponent}.
 *
 * @see <a href="https://screwbox.dev/docs/guides/slosh-physics/">Guide: Slosh phyics</a>
 * @since 2.19.0
 */
public class SloshVolumeRenderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Primary {@link Color} of the slosh volume. Will be used to fill the slosh volume polygon.
     */
    public Color color;

    /**
     * Secondary optional {@link Color} of the slosh volume. Will be used to fill the slosh volume polygon using a gradient.
     */
    public Color secondaryColor;

    /**
     * Surface {@link Color} of the slosh volume. Will be used to draw a line on the surface using {@link #surfaceStrokeWidth}.
     *
     * @since 3.20.0
     */
    public Color surfaceColor = null;

    /**
     * Stroke width used to draw a line on the surface.
     *
     * @since 3.20.0
     */
    public double surfaceStrokeWidth = 2;

    /**
     * Draw order used for rendering.
     *
     * @since 3.16.0
     */
    public int drawOrder;

    public SloshVolumeRenderComponent() {
        this(Color.hex("#777fd8").opacity(0.5), Color.hex("#3445ff").opacity(0.5));
    }

    public SloshVolumeRenderComponent(final Color color) {
        this(color, null);
    }

    public SloshVolumeRenderComponent(final Color color, final Color secondaryColor) {
        this.color = color;
        this.secondaryColor = secondaryColor;
    }
}
