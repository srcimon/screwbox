package de.suzufa.screwbox.core.entities.systems;

import static java.util.Objects.isNull;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.SystemOrder;
import de.suzufa.screwbox.core.entities.components.StateComponent;

@Order(SystemOrder.SIMULATION_BEGIN)
public final class StateSystem implements EntitySystem {

    private static final Archetype STATEFUL_ENTITIES = Archetype.of(StateComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entities().fetchAll(STATEFUL_ENTITIES)) {
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
