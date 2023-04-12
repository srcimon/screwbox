package io.github.srcimon.screwbox.examples.platformer.specials;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.CameraComponent;
import io.github.srcimon.screwbox.core.entities.components.CameraMovementComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class Camera implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        double zoom = object.properties().getDouble("zoom").orElse(3.5);
        int trackedEntityId = object.properties().forceInt("trackedEntity");
        return new Entity().add(
                new CameraMovementComponent(1.5, trackedEntityId),
                new TransformComponent(Bounds.atPosition(object.position(), 0, 0)),
                new CameraComponent(zoom));
    }

}
