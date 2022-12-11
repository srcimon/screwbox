package de.suzufa.screwbox.examples.platformer.collectables;

import static de.suzufa.screwbox.tiled.Tileset.spriteAssetFromJson;

import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.LightOptions;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.CollectableComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class Cherries implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/cherries.json");

    @Override
    public Entity convert(final GameObject object) {
        return new Entity().add(
                new PointLightComponent(LightOptions.glowing(20)
                        .color(Color.RED)
                        .glow(1.6)
                        .glowColor(Color.RED.opacity(0.4))),
                new TransformComponent(object.bounds()),
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
