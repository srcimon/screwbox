package io.github.srcimon.screwbox.vacuum.deathpit;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

public class DeathpitSystem implements EntitySystem {

    private static final Archetype VICTIMS = Archetype.of(TransformComponent.class, DeathpitVictimComponent.class);
    private static final Archetype DEATHPITS = Archetype.of(TransformComponent.class, DeathpitComponent.class);

    @Override
    public void update(Engine engine) {
        final var victims = engine.environment().fetchAll(VICTIMS);
        for (final var deathpit : engine.environment().fetchAll(DEATHPITS)) {
            for (final var victim : victims) {

                if (deathpit.bounds().contains(victim.bounds().expand(4))) {
                    victim.addIfNotPresent(new FallenIntoDeathpitComponent());
                }
            }
        }
    }
}
