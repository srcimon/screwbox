package dev.screwbox.core.environment.light;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.graphics.options.OccluderOptions;

import java.io.Serial;

//TODO test

/**
 * Adds a {@link Light#addBackgdropOccluder(Bounds, OccluderOptions) backgdrop occulder} at the {@link Entity#bounds()}.
 *
 * @since 3.23.0
 */
public class BackdropOccluderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Options used for configuring the shadow.
     */
    public OccluderOptions options;

    public BackdropOccluderComponent(final OccluderOptions options) {
        this.options = options;
    }
}
