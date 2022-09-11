package de.suzufa.screwbox.playground.debo.collectables;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.CollectableComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Tileset;

public class DeboE implements Converter<GameObject> {

    private static final Tileset SPRITES = Tileset
            .fromJson("tilesets/collectables/debo-e.json");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new SpriteComponent(SPRITES.findById(0), object.layer().order()),
                new TransformComponent(object.bounds()),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
