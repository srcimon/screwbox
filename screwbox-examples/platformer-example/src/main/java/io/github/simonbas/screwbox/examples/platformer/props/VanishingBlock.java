package io.github.simonbas.screwbox.examples.platformer.props;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.ColliderComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.VanishingOnCollisionComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

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
