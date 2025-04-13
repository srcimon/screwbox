package io.github.srcimon.screwbox.platformer.collectables;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.rendering.FixedSpinComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.CollectableComponent;
import dev.screwbox.tiles.GameObject;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class DeboO implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/debo-o.json", "animation");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new TransformComponent(object.bounds()),
                new FixedSpinComponent(0.4),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
