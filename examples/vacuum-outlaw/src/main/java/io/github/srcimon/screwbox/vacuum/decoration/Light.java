package io.github.srcimon.screwbox.vacuum.decoration;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.tiled.GameObject;

public class Light implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("light")
                .add(new TransformComponent(object.position()))
                .add(new GlowComponent(18, Color.hex("#feffe9")))
                .add(new PointLightComponent(120, Color.BLACK));
    }
}
