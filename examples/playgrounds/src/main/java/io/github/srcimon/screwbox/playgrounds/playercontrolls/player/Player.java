package io.github.srcimon.screwbox.playgrounds.playercontrolls.player;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.still.StandingState;

public class Player implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(AsciiMap.Tile tile) {
        return new Entity("player")
                .add(new TransformComponent(tile.origin(), 12, 16))
                .add(new PhysicsComponent())
                .add(new CameraTargetComponent())
                .add(new CollisionSensorComponent())
                .add(new CollisionDetailsComponent())
                .add(new StateComponent(new StandingState()));
    }
}
