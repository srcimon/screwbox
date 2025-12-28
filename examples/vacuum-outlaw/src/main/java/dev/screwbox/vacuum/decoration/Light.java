package dev.screwbox.vacuum.decoration;

import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.tiled.GameObject;

public class Light implements Blueprint<GameObject> {

    @Override
    public Entity assembleFrom(GameObject object) {
        return new Entity(object.id()).name("light")
                .add(new TransformComponent(object.position()))
                .add(new GlowComponent(18, Color.hex("#feffe9")))
                .add(new PointLightComponent(120, Color.BLACK));
    }
}
