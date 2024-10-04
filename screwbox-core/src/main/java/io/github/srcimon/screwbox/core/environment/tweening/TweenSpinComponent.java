package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

import java.io.Serial;

/**
 * Links tweening to the spin of the {@link RenderComponent} of an {@link Entity}.
 */
public class TweenSpinComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final boolean isSpinHorizontal;

    public TweenSpinComponent() {
        this(true);
    }

    public TweenSpinComponent(final boolean isSpinHorizontal) {
        this.isSpinHorizontal = isSpinHorizontal;
    }

}
