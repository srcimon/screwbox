package io.github.srcimon.screwbox.examples.platformer.specials;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.camera.CameraComponent;
import io.github.srcimon.screwbox.core.environment.camera.CameraMovementComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class Camera implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        int trackedEntityId = object.properties().getInt("trackedEntity");
        return new Entity("Camera").add(
                new CameraMovementComponent(1.5, trackedEntityId),
                new TransformComponent(Bounds.atPosition(object.position(), 0, 0)));
    }

}
