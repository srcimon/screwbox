package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.StateComponent;

public final class StateSystem implements EntitySystem {

    private static final Archetype STATEFUL_ENTITIES = Archetype.of(StateComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entityEngine().fetchAll(STATEFUL_ENTITIES)) {
            final var stateComponent = entity.get(StateComponent.class);
            final var originalState = stateComponent.state;
            stateComponent.state = originalState.update(entity, engine);
            if (!stateComponent.state.equals(originalState)) {
                originalState.exit(entity, engine);
                stateComponent.state.enter(entity, engine);
            }
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.SIMULATION_BEGIN;
    }
}
