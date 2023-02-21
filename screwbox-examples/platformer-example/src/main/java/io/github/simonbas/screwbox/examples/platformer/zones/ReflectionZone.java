package io.github.simonbas.screwbox.examples.platformer.zones;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.ReflectionComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

public class ReflectionZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        boolean useWaveEffect = object.properties().forceBoolean("useWaveEffect");
        Percent opacityModifier = Percent.of(object.properties().forceDouble("opacityModifier"));

        return new Entity()
                .add(new ReflectionComponent(opacityModifier, useWaveEffect))
                .add(new TransformComponent(object.bounds()));
    }

}
