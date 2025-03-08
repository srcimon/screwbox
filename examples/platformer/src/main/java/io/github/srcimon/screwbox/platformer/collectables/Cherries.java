package io.github.srcimon.screwbox.platformer.collectables;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.rendering.FixedSpinComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOrbitPositionComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.ShaderBundle;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.platformer.components.CollectableComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Cherries implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/collectables/cherries.json", "animation");

    @Override
    public Entity convert(final GameObject object) {
        int x = 0;
        System.out.println(SPRITE.get().allFrames().size());
        for(final var i : SPRITE.get().allFrames()) {
           for(int xp = 0; xp < i.image().getWidth(null); xp++) {
               System.out.println(i.colorAt(xp,0).opacity().value());
               x++;
           }
        }
        return new Entity().add(
             //   new FixedSpinComponent(0.4),
                new PointLightComponent(20, Color.RED),
                new GlowComponent(60, Color.RED.opacity(0.5)),
                new TransformComponent(object.bounds()),
                new RenderComponent(SPRITE.get(), object.layer().order(), SpriteDrawOptions.originalSize().shaderSetup(ShaderBundle.OUTLINE)),
//                new RenderComponent(SPRITE.get(), object.layer().order(), SpriteDrawOptions.originalSize()),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
