package dev.screwbox.playground.gamescene;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.playground.gamescene.teams.ai.ShotComponent;

public class GameFunctions {

    public static void triggerShot(Engine engine, Color team, Vector velocity, Vector position) {
        engine.audio().playSound(SoundBundle.PHASER, SoundOptions.playOnce().position(position));
        engine.environment().addEntity(new Entity(engine.environment().allocateId())
            .add(new ShotComponent(), shot -> shot.team = team)
            .add(new RenderComponent(SpriteBundle.DOT_YELLOW, SpriteDrawOptions.scaled(0.25)))
            .add(new PhysicsComponent(velocity), p -> p.ignoreCollisions = true)
            .add(new TransformComponent(Bounds.atPosition(position, 4, 4)))
        );
    }
}
