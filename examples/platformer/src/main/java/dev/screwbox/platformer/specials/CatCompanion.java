package dev.screwbox.platformer.specials;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.platformer.components.CastShadowComponent;
import dev.screwbox.platformer.components.CatMarkerComponent;
import dev.screwbox.tiled.GameObject;

public class CatCompanion implements Blueprint<GameObject> {

    @Override
    public Entity assembleFrom(final GameObject object) {
        return new Entity(object.id(), "Cat Companion").add(
                new RenderComponent(object.layer().order()),
                new CatMarkerComponent(),
                new CastShadowComponent(),
                new TransformComponent(Bounds.atPosition(object.position(), 10, 24)));
    }

}
