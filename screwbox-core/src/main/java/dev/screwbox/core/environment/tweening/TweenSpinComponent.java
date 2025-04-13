package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.rendering.RenderComponent;

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
