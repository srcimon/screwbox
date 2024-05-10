package io.github.srcimon.screwbox.examples.platformer.collectables;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOrbitPositionComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.CollectableComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Cherries implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/cherries.json");

    @Override
    public Entity convert(final GameObject object) {
        return new Entity().add(
                new TweenOrbitPositionComponent(object.position(), 2),
                new TweenComponent(ofSeconds(2), Ease.LINEAR_IN, true, false),
                new PointLightComponent(20, Color.RED),
                new GlowComponent(45, Color.RED.opacity(0.6)),
                new TransformComponent(object.bounds()),
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new CollisionDetectionComponent(),
                new CollectableComponent());
    }

}
