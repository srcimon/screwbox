package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.TimeoutComponent;

@Order(UpdatePriority.SIMULATION_BEGIN)
public class TimeoutSystem implements EntitySystem {

    private static final Archetype TIMEOUT_ENTITIES = Archetype.of(TimeoutComponent.class);

    @Override
    public void update(Engine engine) {
        Time now = engine.loop().lastUpdate();

        for (var entity : engine.entities().fetchAll(TIMEOUT_ENTITIES)) {
            if (now.isAfter(entity.get(TimeoutComponent.class).timeout)) {
                engine.entities().remove(entity);
            }
        }
    }
}
