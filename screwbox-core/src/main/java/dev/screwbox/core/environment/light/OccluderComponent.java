package dev.screwbox.core.environment.light;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

public class OccluderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Configure if occluder sasts shadows on itself or not.
     */
    public final boolean isAffectedByShadow;

    /**
     * Expand or compact the {@link Bounds} of the shadow casting {@link Entity}.
     *
     * @since 3.7.0
     */
    public int expand;

    public OccluderComponent() {
        this(true);
    }

    public OccluderComponent(final boolean isAffectedByShadow) {
        this.isAffectedByShadow = isAffectedByShadow;
    }

}
