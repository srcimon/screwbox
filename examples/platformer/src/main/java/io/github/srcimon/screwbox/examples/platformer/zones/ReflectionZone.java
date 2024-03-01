package io.github.srcimon.screwbox.examples.platformer.zones;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.ReflectionComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class ReflectionZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        boolean useWaveEffect = object.properties().getBoolean("useWaveEffect");
        Percent opacityModifier = Percent.of(object.properties().getDouble("opacityModifier"));

        return new Entity()
                .add(new ReflectionComponent(opacityModifier, useWaveEffect))
                .add(new TransformComponent(object.bounds()));
    }

}
