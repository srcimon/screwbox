package de.suzufa.screwbox.playground.debo.collectables;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.CollectableComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.SpriteDictionary;

public class DeboE implements Converter<GameObject> {

    private static final SpriteDictionary SPRITES = SpriteDictionary
            .fromJsonTileset("tilesets/collectables/debo-e.json");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new SpriteComponent(SPRITES.findById(0), object.layer().order()),
                new TransformComponent(object.bounds()),
                new CollisionSensorComponent(),
                new CollectableComponent());
    }

}
