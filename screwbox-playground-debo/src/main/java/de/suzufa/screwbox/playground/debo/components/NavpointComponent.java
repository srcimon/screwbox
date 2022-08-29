package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entityengine.Component;
import de.suzufa.screwbox.core.graphics.FlipMode;

public class NavpointComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Class<?> state;
    public final FlipMode flipMode;

    public NavpointComponent(final Class<?> state, final FlipMode flipMode) {
        this.state = state;
        this.flipMode = flipMode;
    }
}
