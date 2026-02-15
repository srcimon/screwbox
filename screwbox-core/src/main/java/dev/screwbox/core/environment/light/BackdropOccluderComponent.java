package dev.screwbox.core.environment.light;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.graphics.options.BackdropShadowOptions;

import java.io.Serial;

//TODO test

/**
 * Add a {@link Light#addBackgdropOccluder(Bounds, BackdropShadowOptions) backgdrop occulder} at the {@link Entity#bounds()}.
 *
 * @since 3.23.0
 */
public class BackdropOccluderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Options used for configuring the shadow.
     */
    public BackdropShadowOptions options;

    public BackdropOccluderComponent(final BackdropShadowOptions options) {
        this.options = options;
    }
}
