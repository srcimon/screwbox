package de.suzufa.screwbox.playground.debo.zones;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class WaterZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity()
                .add(new WaterComponent())
                .add(new TransformComponent(object.bounds()));
    }

}
