package dev.screwbox.vacuum.player;

import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.logic.StateComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.MotionRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.vacuum.player.movement.PlayerWalkingState;

public class Player implements Blueprint<GameObject> {

    @Override
    public Entity create(GameObject object) {
        return new Entity(object.id()).name("player")
                .add(new TransformComponent(object.position(), 10, 10))
                .add(new PlayerComponent())
                .add(new PhysicsComponent())
                .add(new MotionRotationComponent())
                .add(new StateComponent(new PlayerWalkingState()))
                .add(new OccluderComponent(false))
                .add(new RenderComponent(object.layer().order()), render -> render.isSortOrthographic = true)
                .add(new CameraTargetComponent(5));
    }
}
