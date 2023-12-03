package io.github.srcimon.screwbox.examples.platformer.specials;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.SourceImport.Converter;
import io.github.srcimon.screwbox.core.ecosphere.components.CameraComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.CameraMovementComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
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
