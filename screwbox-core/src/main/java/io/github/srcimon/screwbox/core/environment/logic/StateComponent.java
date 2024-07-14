package io.github.srcimon.screwbox.core.environment.logic;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public final class StateComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityState state;

    public StateComponent(final EntityState state) {
        this.state = new InitialEntityState(state); // ensures the state.enter()-Method is invoked on initial state
    }
}
