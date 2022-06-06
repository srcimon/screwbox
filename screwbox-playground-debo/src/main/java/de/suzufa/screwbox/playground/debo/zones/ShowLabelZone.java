package de.suzufa.screwbox.playground.debo.zones;

import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.TriggerAreaComponent;
import de.suzufa.screwbox.core.resources.EntityBatchBuilder.Converter;
import de.suzufa.screwbox.playground.debo.components.LabelComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class ShowLabelZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        String label = object.properties().force("label");
        Integer size = object.properties().getInt("size").orElse(16);
        return new Entity().add(
                new SignalComponent(),
                new LabelComponent(label, size),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new TransformComponent(object.bounds()));
    }

}
