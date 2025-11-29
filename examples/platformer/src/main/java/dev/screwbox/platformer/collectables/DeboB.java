package dev.screwbox.platformer.collectables;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.rendering.FixedSpinComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.CollectableComponent;
import dev.screwbox.tiled.GameObject;

import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class DeboB implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/debo-b.json", "animation");

    @Override
    public Entity convert(final GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE, object.layer().order()),
                new TransformComponent(object.bounds()),
                new FixedSpinComponent(0.4),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
