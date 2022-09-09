package de.suzufa.screwbox.playground.debo.zones;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.ReflectionComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class ReflectionZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        boolean useWaveEffect = object.properties().forceBoolean("useWaveEffect");
        Percentage opacityModifier = Percentage.of(object.properties().forceDouble("opacityModifier"));

        return new Entity()
                .add(new ReflectionComponent(opacityModifier, useWaveEffect))
                .add(new TransformComponent(object.bounds()));
    }

}
