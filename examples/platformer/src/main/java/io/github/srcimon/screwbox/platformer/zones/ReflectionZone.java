package io.github.srcimon.screwbox.platformer.zones;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.ReflectionComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class ReflectionZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        Percent opacityModifier = Percent.of(object.properties().getDouble("opacityModifier"));

        return new Entity()
                .addCustomized(new ReflectionComponent(opacityModifier, object.layer().order()),
                        reflection -> reflection.useWaveEffect = object.properties().tryGetBoolean("useWaveEffect").orElse(false))
                .bounds(object.bounds());
    }

}
