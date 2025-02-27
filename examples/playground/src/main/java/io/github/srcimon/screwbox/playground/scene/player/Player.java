package io.github.srcimon.screwbox.playground.scene.player;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.controls.JumpControlComponent;
import io.github.srcimon.screwbox.core.environment.controls.LeftRightControlComponent;
import io.github.srcimon.screwbox.core.environment.controls.SuspendJumpControlComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.AirFrictionComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playground.scene.player.movement.ClimbComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.DashControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.GrabComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.WallJumpComponent;
import io.github.srcimon.screwbox.playground.scene.player.states.WalkState;

public class Player implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(final AsciiMap.Tile tile) {
        return new Entity().name("player")
                .add(new PhysicsComponent())
                .add(new RenderComponent(Sprite.placeholder(Color.hex("#ee9b00"), Size.of(12, 16))))
                .add(new CameraTargetComponent(3))
                .add(new AirFrictionComponent(400, 0))
                .add(new StateComponent(new WalkState()))

                .add(new CollisionDetailsComponent())
                .add(new CollisionSensorComponent())
                .add(new ClimbComponent(), climb -> {
                    climb.keyUp = ControlKeys.UP;
                    climb.keyDown = ControlKeys.DOWN;
                    climb.speed = 100;
                })
                .add(new DashControlComponent(), dash -> {
                    dash.dashKey = ControlKeys.DASH;
                    dash.upKey = ControlKeys.UP;
                    dash.leftKey = ControlKeys.LEFT;
                    dash.rightKey = ControlKeys.RIGHT;
                    dash.speed = 300;
                })
                .add(new WallJumpComponent(), wallJump -> {
                    wallJump.keyJump = ControlKeys.JUMP;
                    wallJump.keyLeft = ControlKeys.LEFT;
                    wallJump.keyRight = ControlKeys.RIGHT;
                    wallJump.minorAcceleration = 40;
                    wallJump.strongAcceleration = 260;
                })
                .add(new GrabComponent(), grab -> grab.grabKey = ControlKeys.GRAB)
                .add(new SuspendJumpControlComponent())
                .add(new JumpControlComponent(ControlKeys.JUMP), jump -> jump.acceleration = 260)
                .add(new LeftRightControlComponent(ControlKeys.LEFT, ControlKeys.RIGHT), control -> {
                    control.maxSpeed = 90;
                    control.acceleration = 800;
                })
                .add(new TransformComponent(tile.position(), 12, 16));
    }
}
