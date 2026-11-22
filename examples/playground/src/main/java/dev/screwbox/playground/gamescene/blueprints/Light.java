package dev.screwbox.playground.gamescene.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.tiled.GameObject;

public class Light implements Blueprint<GameObject> {
    @Override
    public Entity assembleFrom(GameObject object) {
        final Color color = object.properties().tryGetColor("color").orElse(Color.BLACK);
        final int radius = object.properties().getInt("radius");

        return new Entity()
            .bounds(object.bounds())
            .add(new PointLightComponent(radius, color))
            .add(new GlowComponent(radius / 3.0, color.equals(Color.BLACK) ? Color.WHITE.opacity(0.4) : color.opacity(0.4)));
    }
}
