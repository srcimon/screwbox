package io.github.srcimon.screwbox.examples.platformer.zones;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.SourceImport.Converter;
import io.github.srcimon.screwbox.core.ecosphere.components.ReflectionComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

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
