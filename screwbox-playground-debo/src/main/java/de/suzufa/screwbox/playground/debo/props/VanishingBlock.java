package de.suzufa.screwbox.playground.debo.props;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityBatchImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.playground.debo.components.VanishingOnCollisionComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.TiledSupport;

public class VanishingBlock implements Converter<GameObject> {

    private static final Sprite SPRITE = TiledSupport.loadTileset("tilesets/props/vanishing-block.json").findById(0);

    @Override
    public Entity convert(GameObject object) {
        Integer timeoutMillis = object.properties().getInt("timeout-millis").orElse(300);
        return new Entity().add(
                new ColliderComponent(500),
                new VanishingOnCollisionComponent(Duration.ofMillis(timeoutMillis)),
                new TransformComponent(object.bounds()),
                new SpriteComponent(SPRITE, object.layer().order()));
    }

}
