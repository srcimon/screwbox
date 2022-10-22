package de.suzufa.screwbox.playground.debo.collectables;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.LightGlowComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.LightOptions;
import de.suzufa.screwbox.playground.debo.components.CollectableComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Tileset;

public class Cherries implements Converter<GameObject> {

    private static final Tileset SPRITES = Tileset
            .fromJson("tilesets/collectables/cherries.json");

    @Override
    public Entity convert(final GameObject object) {
        return new Entity().add(
                new LightGlowComponent(30, Color.RED.opacity(0.4)),
                new PointLightComponent(LightOptions.size(40)
                        .color(Color.RED)
                        .glow(0.75)
                        .glowColor(Color.RED.opacity(0.4))),
                new TransformComponent(object.bounds()),
                new SpriteComponent(SPRITES.findById(0), object.layer().order()),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
