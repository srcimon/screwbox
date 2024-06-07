package io.github.srcimon.screwbox.vacuum.player.death;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleComponent;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.CirclesAnimation;
import io.github.srcimon.screwbox.vacuum.player.movement.MovementControlComponent;
import io.github.srcimon.screwbox.vacuum.scenes.GameScene;

public class DeathState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.add(new TweenComponent(Duration.ofMillis(800)));
        entity.add(new TweenScaleComponent(0, 1));
        entity.remove(MovementControlComponent.class);
        entity.get(PhysicsComponent.class).momentum = Vector.zero();
        engine.audio().playSound(SoundBundle.STEAM);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (!entity.hasComponent(TweenComponent.class)) {
            engine.environment().remove(entity);
            engine.scenes()
                    .addOrReplace(new GameScene())
                    .switchTo(GameScene.class, SceneTransition.custom()
                            .outroDurationMillis(500)
                            .outroAnimation(new CirclesAnimation())
                            .introDurationMillis(500)
                            .introAnimation(new CirclesAnimation()));
        }
        return this;
    }
}
