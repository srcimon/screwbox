package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;

import java.io.Serial;

/**
 * Adds a reflection effect to the {@link Entity}. Refects only {@link Entity entities} having a {@link RenderComponent}
 * which are above the refection area having a draw order below the specified value. Gets processed by
 * {@link RenderSystem}.
 */
public class ReflectionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The opacity of the resulting reflection.
     */
    public final Percent opacityModifier;

    /**
     * THe draw order within the resulting {@link SpriteBatch}.
     */
    public int drawOrder;

//TODO add javadoc
    public boolean useWaveEffect = false;
    public boolean applyWavePostfilter = false;
    public double speed = 0.005;
    public double amplitude = 2.0;
    public double frequenzy = 0.5;

    public ReflectionComponent(final Percent opacityModifier, final int drawOrder) {
        this.opacityModifier = opacityModifier;
        this.drawOrder = drawOrder;
    }
}
