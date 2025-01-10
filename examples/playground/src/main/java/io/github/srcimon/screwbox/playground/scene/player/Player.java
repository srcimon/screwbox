package io.github.srcimon.screwbox.playground.scene.player;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playground.movement.MovementControlComponent;

public class Player implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(final AsciiMap.Tile tile) {
        return new Entity().name("player")
                .add(new PhysicsComponent())
                .add(new RenderComponent(SpriteBundle.SLIME_MOVING))
                .add(new CameraTargetComponent(1000))
                .addCustomized(new MovementControlComponent(), control -> {
                    control.left = ControlKeys.LEFT;
                    control.right = ControlKeys.RIGHT;
                    control.acceleration = 1400;
                    control.maxSpeed = 1000;
                })
                .add(new TransformComponent(tile.bounds()));
    }
}
