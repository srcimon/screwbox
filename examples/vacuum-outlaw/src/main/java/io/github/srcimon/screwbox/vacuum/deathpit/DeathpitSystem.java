package io.github.srcimon.screwbox.vacuum.deathpit;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class DeathpitSystem implements EntitySystem {

    private static final Archetype VICTIMS = Archetype.ofSpacial(DeathpitVictimComponent.class);
    private static final Archetype DEATHPITS = Archetype.ofSpacial(DeathpitComponent.class);

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
