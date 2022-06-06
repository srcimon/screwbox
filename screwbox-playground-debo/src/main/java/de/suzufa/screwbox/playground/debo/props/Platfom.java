package de.suzufa.screwbox.playground.debo.props;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityBatchImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.MovingPlattformComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.SpriteDictionary;
import de.suzufa.screwbox.tiled.TiledSupport;

public class Platfom implements Converter<GameObject> {

    private static final SpriteDictionary SPRITE = TiledSupport.loadTileset("tilesets/props/platform.json");

    @Override
    public Entity convert(GameObject object) {
        double speed = object.properties().getDouble("speed").orElse(60.0);
        return new Entity().add(
                new ColliderComponent(500, Percentage.min(), true),
                new SpriteComponent(SPRITE.findById(0), object.layer().order()),
                new TransformComponent(Bounds.atPosition(object.position(), 48, 12)),
                new CollisionSensorComponent(),
                new MovingPlattformComponent(object.properties().forceInt("waypoint"), speed));
    }

}
