package io.github.srcimon.screwbox.vacuum.player;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.MovementRotationComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.vacuum.player.movement.PlayerWalkingState;

public class Player implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("player")
                .add(new TransformComponent(object.position(), 10, 10))
                .add(new PlayerComponent())
                .add(new PhysicsComponent())
                .add(new MovementRotationComponent())
                .add(new StateComponent(new PlayerWalkingState()))
                .add(new ShadowCasterComponent(false))
                .addCustomized(new RenderComponent(object.layer().order()),
                        render -> render.options = render.options.useOrhographicSorting())
                .add(new CameraTargetComponent(5));
    }
}
