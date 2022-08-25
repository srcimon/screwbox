package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.TimeoutComponent;

public class TimeoutSystem implements EntitySystem {

    private static final Archetype TIMEOUT_ENTITIES = Archetype.of(TimeoutComponent.class);

    @Override
    public void update(Engine engine) {
        Time now = engine.loop().lastUpdate();

        for (var entity : engine.entityEngine().fetchAll(TIMEOUT_ENTITIES)) {
            if (now.isAfter(entity.get(TimeoutComponent.class).timeout)) {
                engine.entityEngine().remove(entity);
            }
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.SIMULATION_BEGIN;
    }
}
