package de.suzufa.screwbox.playground.debo.zones;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.ReflectionComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
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
