package io.github.srcimon.screwbox.vacuum.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class DashingSystem implements EntitySystem {

    private static final Archetype DASHINGS = Archetype.of(DashingComponent.class);

    @Override
    public void update(Engine engine) {
        for(final var dashing : engine.environment().fetchAll(DASHINGS)) {

        }
    }
}
