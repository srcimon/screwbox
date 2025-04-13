package io.github.srcimon.screwbox.platformer.zones;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.rendering.ReflectionComponent;
import dev.screwbox.tiles.GameObject;

public class ReflectionZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        Percent opacityModifier = Percent.of(object.properties().getDouble("opacityModifier"));

        return new Entity()
                .add(new ReflectionComponent(opacityModifier, object.layer().order()),
                        reflection -> reflection.applyWaveDistortionProjection = object.properties().tryGetBoolean("useWaveEffect").orElse(false))
                .bounds(object.bounds());
    }

}
