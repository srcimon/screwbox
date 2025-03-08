package io.github.srcimon.screwbox.platformer.collectables;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.rendering.FixedSpinComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.CollectableComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Cherries implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/cherries.json", "animation");

    @Override
    public Entity convert(final GameObject object) {
        return new Entity().add(
                new FixedSpinComponent(0.4),
                new PointLightComponent(20, Color.RED),
                new GlowComponent(60, Color.RED.opacity(0.5)),
                new TransformComponent(object.bounds()),
                new RenderComponent(SPRITE, object.layer().order()),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
