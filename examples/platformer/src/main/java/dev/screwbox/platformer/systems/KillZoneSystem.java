package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.platformer.components.DeathEventComponent;
import dev.screwbox.platformer.components.KillZoneComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

import java.util.Optional;

public class KillZoneSystem implements EntitySystem {

    private static final Archetype TRIGGER_AREAS = Archetype.of(TriggerAreaComponent.class, KillZoneComponent.class);
    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity area : engine.environment().fetchAll(TRIGGER_AREAS)) {
            if (area.get(TriggerAreaComponent.class).isTriggered) {
                Optional<Entity> fetch = engine.environment().tryFetchSingleton(PLAYER);
                if (fetch.isPresent() && !fetch.get().hasComponent(DeathEventComponent.class)) {
                    fetch.get().add(new DeathEventComponent(area.get(KillZoneComponent.class).deathType));
                }
            }
        }
    }

}
