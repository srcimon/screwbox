package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Tweens shader progress of {@link Entity}.
 *
 * @since 2.18.0
 */
public class TweenShaderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final boolean invert;

    /**
     * Creates a new instance.
     */
    public TweenShaderComponent() {
        this(false);
    }

    /**
     * Creates a new instance with inverted progression.
     */
    public TweenShaderComponent(final boolean invert) {
        this.invert = invert;
    }
}
