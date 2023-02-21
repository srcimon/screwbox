package io.github.simonbas.screwbox.examples.platformer.collectables;

import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.CollisionSensorComponent;
import io.github.simonbas.screwbox.core.entities.components.PointLightComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.LightOptions;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.CollectableComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

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
