package dev.screwbox.core.environment.light;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

public class ShadowCasterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final boolean selfShadow;

    /**
     * Expand or compact the {@link Bounds} of the shadow casting {@link Entity}.
     *
     * @since 3.7.0
     */
    public int expand;

    public ShadowCasterComponent() {
        this(true);
    }

    public ShadowCasterComponent(final boolean selfShadow) {
        this.selfShadow = selfShadow;
    }

}
