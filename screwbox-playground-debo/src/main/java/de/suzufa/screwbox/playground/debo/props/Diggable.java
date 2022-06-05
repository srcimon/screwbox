package de.suzufa.screwbox.playground.debo.props;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.resources.EntityConverter;
import de.suzufa.screwbox.playground.debo.components.DiggableComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.TiledSupport;

public class Diggable implements EntityConverter<GameObject> {

    @Override
    public boolean accepts(GameObject object) {
        return "diggable".equals(object.name());
    }

    @Override
    public Entity convert(GameObject object) {
        Sprite sprite = TiledSupport.loadTileset("tilesets/props/diggable.json").findById(0);
        return new Entity().add(
                new SpriteComponent(sprite, object.layer().order()),
                new DiggableComponent(),
                new TransformComponent(object.bounds()),
                new ColliderComponent(500, Percentage.min()));
    }

}
