package dev.screwbox.vacuum.decoration;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.tiled.GameObject;

public class Light implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("light")
                .add(new TransformComponent(object.position()))
                .add(new GlowComponent(18, Color.hex("#feffe9")))
                .add(new PointLightComponent(120, Color.BLACK));
    }
}
