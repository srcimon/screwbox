package dev.screwbox.core.environment.slosh;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Will apply waves when on liquid when in contact with the surface.
 *
 * @see <a href="https://screwbox.dev/docs/guides/slosh-physics/">Guide: Slosh phyics</a>
 * @since 2.19.0
 */
public class SloshInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Speed changes below will be ignored to avoid endless interaction loop when standing still.
     */
    public final double threshold;

    /**
     * Modifier for horizontal impact.
     */
    public double xModifier;

    /**
     * Modifier for vertical impact. Normally double the value of horizontal impact.
     */
    public double yModifier;

    public SloshInteractionComponent() {
        this(1.5, 30);
    }

    public SloshInteractionComponent(final double yModifier, final double threshold) {
        this.yModifier = yModifier;
        this.xModifier = yModifier / 2.0;
        this.threshold = threshold;
    }
}
