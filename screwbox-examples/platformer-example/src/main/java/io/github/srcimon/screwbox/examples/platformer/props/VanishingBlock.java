package io.github.srcimon.screwbox.examples.platformer.props;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.VanishingOnCollisionComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

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
