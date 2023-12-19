package io.github.srcimon.screwbox.core.environment.logic;

import io.github.srcimon.screwbox.core.environment.Component;

public final class StateComponent implements Component {

    private static final long serialVersionUID = 1L;

    public EntityState state;

    public StateComponent(final EntityState state) {
        this.state = new InitialEntityState(state); // ensures the state.enter()-Method is invoked on initial state
    }
}
