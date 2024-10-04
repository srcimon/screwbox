package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Sprite;

import java.io.Serial;

/**
 * Applies a continuous spin to the {@link Sprite} of an {@link RenderComponent}.
 */
public class FixedSpinComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double spinsPerSecond;
    public final boolean isSpinHorizontal;

    public FixedSpinComponent(final double spinsPerSecond) {
        this(spinsPerSecond, true);
    }

    public FixedSpinComponent(final double spinsPerSecond, final boolean isSpinHorizontal) {
        this.spinsPerSecond = spinsPerSecond;
        this.isSpinHorizontal = isSpinHorizontal;
    }
}
