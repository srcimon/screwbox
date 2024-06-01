package io.github.srcimon.screwbox.vacuum.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class DashSystem implements EntitySystem {

    private static final Archetype DASHERS = Archetype.of(DashComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var dasher : engine.environment().fetchAll(DASHERS)) {
            final var dash = dasher.get(DashComponent.class);
            dasher.get(PhysicsComponent.class).momentum = dash.force;
            final var dashProgress = dash.duration.progress(dash.started, engine.loop().lastUpdate());
            if (dashProgress.isMax()) {
                dasher.remove(DashComponent.class);
            }
        }
    }
}
