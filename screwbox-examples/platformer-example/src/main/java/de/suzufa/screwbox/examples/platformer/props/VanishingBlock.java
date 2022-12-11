package de.suzufa.screwbox.examples.platformer.props;

import static de.suzufa.screwbox.tiled.Tileset.spriteAssetFromJson;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.VanishingOnCollisionComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class VanishingBlock implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/vanishing-block.json");

    @Override
    public Entity convert(GameObject object) {
        Integer timeoutMillis = object.properties().getInt("timeout-millis").orElse(300);
        return new Entity().add(
                new ColliderComponent(500),
                new VanishingOnCollisionComponent(Duration.ofMillis(timeoutMillis)),
                new TransformComponent(object.bounds()),
                new RenderComponent(SPRITE.get(), object.layer().order()));
    }

}
