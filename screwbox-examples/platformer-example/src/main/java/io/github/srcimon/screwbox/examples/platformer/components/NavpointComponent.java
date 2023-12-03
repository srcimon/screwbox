package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.ecosphere.Component;
import io.github.srcimon.screwbox.core.graphics.Flip;

public class NavpointComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Class<?> state;
    public final Flip flipMode;

    public NavpointComponent(final Class<?> state, final Flip flipMode) {
        this.state = state;
        this.flipMode = flipMode;
    }
}
