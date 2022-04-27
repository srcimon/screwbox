package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entityengine.Component;

public class NavpointComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Class<?> state;
    public final boolean flipped;

    public NavpointComponent(final Class<?> state, final boolean flipped) {
        this.state = state;
        this.flipped = flipped;
    }
}
