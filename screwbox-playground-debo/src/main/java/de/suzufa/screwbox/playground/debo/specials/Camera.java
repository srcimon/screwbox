package de.suzufa.screwbox.playground.debo.specials;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityBatchImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.CameraComponent;
import de.suzufa.screwbox.core.entityengine.components.CameraMovementComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.tiled.GameObject;

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
