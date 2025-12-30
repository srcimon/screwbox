package dev.screwbox.platformer.collectables;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.rendering.FixedSpinComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.CollectableComponent;
import dev.screwbox.tiled.GameObject;

import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Cherries implements Blueprint<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/cherries.json", "animation");

    @Override
    public Entity assembleFrom(final GameObject object) {
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
