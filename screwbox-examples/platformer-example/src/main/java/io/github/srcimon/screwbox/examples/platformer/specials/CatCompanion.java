package io.github.srcimon.screwbox.examples.platformer.specials;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.CastShadowComponent;
import io.github.srcimon.screwbox.examples.platformer.components.CatMarkerComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class CatCompanion implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id()).add(
                new RenderComponent(object.layer().order()),
                new CatMarkerComponent(),
                new CastShadowComponent(),
                new TransformComponent(Bounds.atPosition(object.position(), 10, 24)));
    }

}
