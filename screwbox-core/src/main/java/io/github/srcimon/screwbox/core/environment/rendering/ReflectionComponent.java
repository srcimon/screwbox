package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds a reflection effect to the {@link Entity}. Refects only {@link Entity entities} having a {@link RenderComponent}
 * which are above the refection area having a draw order below the specified value. Gets processed by
 * {@link RenderSystem}.
 */
public class ReflectionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Percent opacityModifier;
    public int drawOrder;
    public boolean useWaveEffect = false;
    public boolean applyWavePostfilter = false;

    public ReflectionComponent(final Percent opacityModifier, final int drawOrder) {
        this.opacityModifier = opacityModifier;
        this.drawOrder = drawOrder;
    }
}
