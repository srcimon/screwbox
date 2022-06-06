package de.suzufa.screwbox.playground.debo.specials;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.resources.EntityBatchBuilder.Converter;
import de.suzufa.screwbox.playground.debo.components.CastShadowComponent;
import de.suzufa.screwbox.playground.debo.components.CatMarkerComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class CatCompanion implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id()).add(
                new SpriteComponent(object.layer().order()),
                new CatMarkerComponent(),
                new CastShadowComponent(),
                new TransformComponent(Bounds.atPosition(object.position(), 10, 24)));
    }

}
