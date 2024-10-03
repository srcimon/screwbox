package io.github.srcimon.screwbox.platformer.collectables;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSpinComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.CollectableComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class DeboE implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/debo-e.json", "animation");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new TransformComponent(object.bounds()),
                new TweenSpinComponent(),
                new TweenComponent(Duration.ofSeconds(2), Ease.LINEAR_IN, true),
                new CollisionDetectionComponent(),
                new CollectableComponent());
    }

}
