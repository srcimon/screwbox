package de.suzufa.screwbox.playground.debo.collectables;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.ReflectedComponent;
import de.suzufa.screwbox.playground.debo.components.CollectableComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Tileset;

public class Cherries implements Converter<GameObject> {

    private static final Tileset SPRITES = Tileset
            .fromJson("tilesets/collectables/cherries.json");

    @Override
    public Entity convert(final GameObject object) {
        return new Entity().add(
                new ReflectedComponent(),
                new TransformComponent(object.bounds()),
                new SpriteComponent(SPRITES.findById(0), object.layer().order()),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
