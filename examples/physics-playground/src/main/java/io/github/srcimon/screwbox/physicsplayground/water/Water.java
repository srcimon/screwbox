package io.github.srcimon.screwbox.physicsplayground.water;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.ReflectionComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class Water implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("water")
                .add(new TransformComponent(object.bounds()))
                .addCustomized(new ReflectionComponent(Percent.half(), object.layer().order()),
                        reflection -> reflection.applyWaveDistortionPostfilter = true);
    }
}
