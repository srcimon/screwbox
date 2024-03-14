package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.io.Serial;

public class NavpointComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Class<?> state;
    public final SpriteDrawOptions.Flip flipMode;

    public NavpointComponent(final Class<?> state, final SpriteDrawOptions.Flip flipMode) {
        this.state = state;
        this.flipMode = flipMode;
    }
}
