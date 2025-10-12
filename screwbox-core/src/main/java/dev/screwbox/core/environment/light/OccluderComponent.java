package dev.screwbox.core.environment.light;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

public class OccluderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Casts shadows on itself or nor.
     */
    public final boolean isSelfOcclude;

    /**
     * Expand or compact the {@link Bounds} of the shadow casting {@link Entity}.
     *
     * @since 3.7.0
     */
    public int expand;

    public OccluderComponent() {
        this(true);
    }

    public OccluderComponent(final boolean isSelfOcclude) {
        this.isSelfOcclude = isSelfOcclude;
    }

}
