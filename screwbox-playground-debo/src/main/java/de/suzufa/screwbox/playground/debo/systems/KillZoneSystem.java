package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;
import de.suzufa.screwbox.playground.debo.components.KillZoneComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

public class KillZoneSystem implements EntitySystem {

    private static final Archetype TRIGGER_AREAS = Archetype.of(SignalComponent.class, KillZoneComponent.class);
    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity area : engine.entityEngine().fetchAll(TRIGGER_AREAS)) {
            if (area.get(SignalComponent.class).isTriggered) {
                for (Entity playerEntity : engine.entityEngine().fetchAll(PLAYER)) {
                    if (!playerEntity.hasComponent(DeathEventComponent.class)) {
                        playerEntity.add(new DeathEventComponent(area.get(KillZoneComponent.class).deathType));
                    }
                }
            }
        }
    }

}
