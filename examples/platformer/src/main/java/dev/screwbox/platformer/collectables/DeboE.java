package dev.screwbox.platformer.collectables;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.rendering.FixedSpinComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.CollectableComponent;
import dev.screwbox.tiled.GameObject;

import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class DeboE implements Blueprint<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/debo-e.json", "animation");

    @Override
    public Entity assembleFrom(final GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new TransformComponent(object.bounds()),
                new FixedSpinComponent(0.4),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
