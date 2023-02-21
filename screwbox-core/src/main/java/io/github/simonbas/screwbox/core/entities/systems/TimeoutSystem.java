package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.Order;
import io.github.simonbas.screwbox.core.entities.SystemOrder;
import io.github.simonbas.screwbox.core.entities.components.TimeoutComponent;

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
