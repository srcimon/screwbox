package dev.screwbox.core.environment.light;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.graphics.options.ShadowOptions;

import java.io.Serial;

/**
 * Adds a {@link Light#addBackgdropOccluder(Bounds, ShadowOptions) backgdrop occulder} at the {@link Entity#bounds()}.
 *
 * @since 3.23.0
 */
public class BackdropOccluderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Options used for configuring the shadow.
     */
    public ShadowOptions options;

    public BackdropOccluderComponent(final ShadowOptions options) {
        this.options = options;
    }
}
