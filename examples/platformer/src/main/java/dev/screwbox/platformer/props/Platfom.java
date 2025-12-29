package dev.screwbox.platformer.props;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.MovingPlatformComponent;
import dev.screwbox.tiled.GameObject;

import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Platfom implements Blueprint<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/platform.json");

    @Override
    public Entity assembleFrom(GameObject object) {
        final double speed = object.properties().tryGetDouble("speed").orElse(60.0);
        return new Entity().add(new ColliderComponent(500, Percent.zero(), true))
                .add(new RenderComponent(SPRITE, object.layer().order()))
                .add(new TransformComponent(Bounds.atPosition(object.position(), 48, 12)))
                .add(new CollisionSensorComponent(), sensor -> sensor.range = 1)
                .add(new OccluderComponent(false), x -> x.expand = -2)
                .add(new MovingPlatformComponent(object.properties().getInt("waypoint"), speed));
    }

}
