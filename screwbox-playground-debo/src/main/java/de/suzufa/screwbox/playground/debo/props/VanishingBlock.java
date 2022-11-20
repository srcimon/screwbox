package de.suzufa.screwbox.playground.debo.props;

import static de.suzufa.screwbox.tiled.Tileset.loadSpriteAsset;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.playground.debo.components.VanishingOnCollisionComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class VanishingBlock implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = loadSpriteAsset("tilesets/props/vanishing-block.json");

    @Override
    public Entity convert(GameObject object) {
        Integer timeoutMillis = object.properties().getInt("timeout-millis").orElse(300);
        return new Entity().add(
                new ColliderComponent(500),
                new VanishingOnCollisionComponent(Duration.ofMillis(timeoutMillis)),
                new TransformComponent(object.bounds()),
                new SpriteComponent(SPRITE.get(), object.layer().order()));
    }

}
