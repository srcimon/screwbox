package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.Order;
import io.github.srcimon.screwbox.core.entities.SystemOrder;
import io.github.srcimon.screwbox.core.entities.components.TimeoutComponent;

@Order(SystemOrder.SIMULATION_BEGIN)
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
