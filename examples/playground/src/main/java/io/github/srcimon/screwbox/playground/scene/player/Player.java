package io.github.srcimon.screwbox.playground.scene.player;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playground.movement.AirFrictionComponent;
import io.github.srcimon.screwbox.playground.movement.ClimbComponent;
import io.github.srcimon.screwbox.playground.movement.JumpComponent;
import io.github.srcimon.screwbox.playground.movement.MovementControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.states.JumpState;
import io.github.srcimon.screwbox.playground.scene.player.states.WalkState;

public class Player implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(final AsciiMap.Tile tile) {
        return new Entity().name("player")
                .add(new PhysicsComponent())
                .add(new RenderComponent(Sprite.placeholder(Color.hex("#ee9b00"), Size.of(8, 12))))
                .add(new CameraTargetComponent(3))
                .add(new AirFrictionComponent(400))
                .add(new StateComponent(new WalkState()))
                .addCustomized(new ClimbComponent(), climb -> {
                    climb.grabKey = ControlKeys.GRAB;
                    climb.isEnabled = false;
                })
                .add(new CollisionDetailsComponent())
                .add(new CollisionSensorComponent())
                .addCustomized(new JumpComponent(), jump -> {
                    jump.key = ControlKeys.JUMP;
                    jump.acceleration = 260;
                    jump.jumpState = new JumpState();
                })
                .addCustomized(new MovementControlComponent(), control -> {
                    control.left = ControlKeys.LEFT;
                    control.right = ControlKeys.RIGHT;
                    control.maxSpeed = 150;
                    control.acceleration = 800;
                })
                .add(new TransformComponent(tile.position(), 8, 12));
    }
}
