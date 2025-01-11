package io.github.srcimon.screwbox.playground.scene.player;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playground.movement.AirFrictionComponent;
import io.github.srcimon.screwbox.playground.movement.JumpComponent;
import io.github.srcimon.screwbox.playground.movement.MovementControlComponent;

public class Player implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(final AsciiMap.Tile tile) {
        return new Entity().name("player")
                .add(new PhysicsComponent())
                .add(new RenderComponent(Sprite.placeholder(Color.hex("#5d7ebc"),tile.size())))
                .add(new CameraTargetComponent(800))
                .add(new AirFrictionComponent(400))
                .add(new CollisionDetailsComponent())
                .addCustomized(new JumpComponent(), jump -> {
                    jump.key = ControlKeys.JUMP;
                    jump.acceleration = 260;
                })
                .addCustomized(new MovementControlComponent(), control -> {
                    control.left = ControlKeys.LEFT;
                    control.right = ControlKeys.RIGHT;
                    control.maxSpeed = 150;
                    control.acceleration = 800;
                })
                .add(new TransformComponent(tile.bounds()));
    }
}
