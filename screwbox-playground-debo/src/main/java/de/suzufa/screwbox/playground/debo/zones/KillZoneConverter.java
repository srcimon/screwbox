package de.suzufa.screwbox.playground.debo.zones;

import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.entityengine.components.TriggerAreaComponent;
import de.suzufa.screwbox.playground.debo.components.KillZoneComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent.DeathType;
import de.suzufa.screwbox.tiled.Converter;
import de.suzufa.screwbox.tiled.GameObject;

public class KillZoneConverter implements Converter<GameObject> {

    @Override
    public boolean accepts(GameObject object) {
        return "killzone".equals(object.name());
    }

    @Override
    public Entity convert(GameObject object) {
        String deathType = object.properties().get("death-type").orElse("SPIKES");

        return new Entity().add(
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new KillZoneComponent(DeathType.valueOf(deathType)),
                new TransformComponent(object.bounds()));
    }

}
