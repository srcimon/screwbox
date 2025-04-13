package dev.screwbox.core.environment.logic;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds a <a href="https://en.wikipedia.org/wiki/Finite-state_machine">finite state machine</a> to an {@link Entity}.
 * Can be used to add behaviour to state changes like 'attacking' or 'jumping'.
 */
public final class StateComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Current {@link EntityState}. Should not be changed after initialization. Will be updated automatically by {@link StateSystem}.
     */
    public EntityState state;

    /**
     * Can be used to set the next {@link EntityState}. This will skip the {@link EntityState#update(Entity, Engine)}
     * invocation of the current state. Should be used with care.
     *
     * @since 2.11.0
     */
    public EntityState forcedState;

    /**
     * Creates a new instance and sets its initial state.
     */
    public StateComponent(final EntityState state) {
        this.state = new InitialEntityState(state); // ensures the state.enter()-Method is invoked on initial state
    }
}
