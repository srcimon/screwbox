package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.components.StateComponent;
import io.github.srcimon.screwbox.core.environment.*;

import static java.util.Objects.isNull;

@Order(SystemOrder.SIMULATION_BEGIN)
public final class StateSystem implements EntitySystem {

    private static final Archetype STATEFUL_ENTITIES = Archetype.of(StateComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(STATEFUL_ENTITIES)) {
            final var stateComponent = entity.get(StateComponent.class);
            final var originalState = stateComponent.state;
            stateComponent.state = originalState.update(entity, engine);
            if (isNull(stateComponent.state)) {
                throw new IllegalStateException(
                        "Next state must not be null. Returned from EntityState: "
                                + originalState.getClass().getSimpleName());
            }
            if (!stateComponent.state.equals(originalState)) {
                originalState.exit(entity, engine);
                stateComponent.state.enter(entity, engine);
            }
        }
    }
}
