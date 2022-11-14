package de.suzufa.screwbox.playground.debo.props;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.ShadowCasterComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.MovingPlatformComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Tileset;

public class Platfom implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        double speed = object.properties().getDouble("speed").orElse(60.0);
        return new Entity().add(
                new ColliderComponent(500, Percent.min(), true),
                new SpriteComponent(Tileset.fromJson("tilesets/props/platform.json").single(), object.layer().order()),
                new TransformComponent(Bounds.atPosition(object.position(), 48, 12)),
                new CollisionSensorComponent(),
                new ShadowCasterComponent(),
                new MovingPlatformComponent(object.properties().forceInt("waypoint"), speed));
    }

}
