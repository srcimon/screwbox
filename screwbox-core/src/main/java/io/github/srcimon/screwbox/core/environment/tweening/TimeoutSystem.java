package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
//TODO: javadoc and tests
public class TimeoutSystem implements EntitySystem {

    private static final Archetype TIMEOUT_ENTITIES = Archetype.of(TimeoutComponent.class);

    @Override
    public void update(Engine engine) {
        Time now = engine.loop().lastUpdate();

        for (var entity : engine.environment().fetchAll(TIMEOUT_ENTITIES)) {
            if (now.isAfter(entity.get(TimeoutComponent.class).timeout)) {
                engine.environment().remove(entity);
            }
        }
    }
}
