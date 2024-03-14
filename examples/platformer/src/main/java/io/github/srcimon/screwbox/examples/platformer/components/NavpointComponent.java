package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class NavpointComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Class<?> state;
    public boolean isFlippedHorizontally;

    public NavpointComponent(final Class<?> state, final boolean isFlippedHorizontally) {
        this.state = state;
        this.isFlippedHorizontally = isFlippedHorizontally;
    }
}
