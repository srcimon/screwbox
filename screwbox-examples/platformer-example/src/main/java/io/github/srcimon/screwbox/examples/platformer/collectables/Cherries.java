package io.github.srcimon.screwbox.examples.platformer.collectables;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.components.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LightOptions;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.CollectableComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Cherries implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/cherries.json", "animation");

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
