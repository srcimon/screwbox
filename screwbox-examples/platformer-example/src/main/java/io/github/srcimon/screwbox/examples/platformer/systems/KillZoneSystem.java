package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.SignalComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.examples.platformer.components.KillZoneComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;

import java.util.Optional;

public class KillZoneSystem implements EntitySystem {

    private static final Archetype TRIGGER_AREAS = Archetype.of(SignalComponent.class, KillZoneComponent.class);
    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity area : engine.entities().fetchAll(TRIGGER_AREAS)) {
            if (area.get(SignalComponent.class).isTriggered) {
                Optional<Entity> fetch = engine.entities().fetch(PLAYER);
                if (fetch.isPresent() && !fetch.get().hasComponent(DeathEventComponent.class)) {
                    fetch.get().add(new DeathEventComponent(area.get(KillZoneComponent.class).deathType));
                }
            }
        }
    }

}
