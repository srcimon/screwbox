package de.suzufa.screwbox.playground.debo.props;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterReflectionComponent;
import de.suzufa.screwbox.playground.debo.components.MovableComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Tileset;

public class Box implements Converter<GameObject> {

    private static final Tileset SPRITES = Tileset.fromJson("tilesets/props/box.json");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new WaterReflectionComponent(),
                new SpriteComponent(SPRITES.findById(0), object.layer().order()),
                new PhysicsBodyComponent(),
                new MovableComponent(),
                new TransformComponent(object.bounds()),
                new ColliderComponent(500, Percentage.min()));
    }

}
