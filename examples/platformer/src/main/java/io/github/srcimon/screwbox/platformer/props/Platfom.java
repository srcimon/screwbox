package io.github.srcimon.screwbox.platformer.props;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.ShadowCasterComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.MovingPlatformComponent;
import dev.screwbox.tiles.GameObject;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class Platfom implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/platform.json");

    @Override
    public Entity convert(GameObject object) {
        double speed = object.properties().tryGetDouble("speed").orElse(60.0);
        return new Entity().add(
                new ColliderComponent(500, Percent.zero(), true),
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new TransformComponent(Bounds.atPosition(object.position(), 48, 12)),
                new CollisionSensorComponent(),
                new ShadowCasterComponent(),
                new MovingPlatformComponent(object.properties().getInt("waypoint"), speed));
    }

}
