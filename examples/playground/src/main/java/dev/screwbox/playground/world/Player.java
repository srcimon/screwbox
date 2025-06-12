package dev.screwbox.playground.world;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlComponent;
import dev.screwbox.core.environment.physics.AirFrictionComponent;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.generation.AsciiMap;

public class Player implements SourceImport.Converter<AsciiMap.Tile> {

    @Override
    public Entity convert(AsciiMap.Tile tile) {
        return new Entity()
                .name("player")
                .bounds(tile.bounds())
                .add(new SuspendJumpControlComponent())
                .add(new CollisionDetailsComponent())
                .add(new CollisionSensorComponent())
                .add(new JumpControlComponent())
                .add(new LeftRightControlComponent())
                .add(new PhysicsComponent())
                .add(new CameraTargetComponent(10))
                .add(new AirFrictionComponent(80))
                .add(new RenderComponent(Sprite.placeholder(Color.BLUE, tile.size())));
    }
}
