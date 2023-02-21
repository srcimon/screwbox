package io.github.simonbas.screwbox.examples.platformer.props;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.*;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.MovingPlatformComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Platfom implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/platform.json");

    @Override
    public Entity convert(GameObject object) {
        double speed = object.properties().getDouble("speed").orElse(60.0);
        return new Entity().add(
                new ColliderComponent(500, Percent.min(), true),
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new TransformComponent(Bounds.atPosition(object.position(), 48, 12)),
                new CollisionSensorComponent(),
                new ShadowCasterComponent(),
                new MovingPlatformComponent(object.properties().forceInt("waypoint"), speed));
    }

}
