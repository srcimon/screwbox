package dev.screwbox.core.environment.logic;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public final class StateSystem implements EntitySystem {

    private static final Archetype STATEFUL_ENTITIES = Archetype.of(StateComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(STATEFUL_ENTITIES)) {
            final var stateComponent = entity.get(StateComponent.class);

            final var originalState = stateComponent.state;

            stateComponent.state = nonNull(stateComponent.forcedState)
                    ? stateComponent.forcedState
                    : originalState.update(entity, engine);

            if (isNull(stateComponent.state)) {
                throw new IllegalStateException(
                        "Next state must not be null. Returned from EntityState: "
                                + originalState.getClass().getSimpleName());
            }
            if (!stateComponent.state.equals(originalState)) {
                originalState.exit(entity, engine);
                stateComponent.state.enter(entity, engine);
            }
            stateComponent.forcedState = null;
        }
    }
}
