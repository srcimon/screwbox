package dev.screwbox.platformer.specials;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.platformer.components.CastShadowComponent;
import dev.screwbox.platformer.components.CatMarkerComponent;
import dev.screwbox.tiled.GameObject;

public class CatCompanion implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id(), "Cat Companion").add(
                new RenderComponent(object.layer().order()),
                new CatMarkerComponent(),
                new CastShadowComponent(),
                new TransformComponent(Bounds.atPosition(object.position(), 10, 24)));
    }

}
