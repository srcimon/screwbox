package de.suzufa.screwbox.playground.debo.systems;

import java.util.Optional;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.SignalComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;
import de.suzufa.screwbox.playground.debo.components.KillZoneComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

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
