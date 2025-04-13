package io.github.srcimon.screwbox.vacuum.enemies;

import dev.screwbox.core.Engine;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.particles.ParticleOptions;

import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.graphics.options.CameraShakeOptions.lastingForDuration;

public class HurtSystem implements EntitySystem {

    private static final Archetype HURT_ENEMIES = Archetype.ofSpacial(EnemyComponent.class, HurtComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var enemy : engine.environment().fetchAll(HURT_ENEMIES)) {
            engine.audio().playSound(SoundBundle.SPLASH, SoundOptions.playOnce().position(enemy.position()));
            engine.graphics().camera().shake(lastingForDuration(ofMillis(200)).strength(20));
            engine.particles().spawnMultiple(8, enemy.position(), ParticleOptions.particleSource(enemy)
                    .lifetimeSeconds(1)
                    .randomBaseSpeed(20)
                    .randomStartScale(0.5, 0.8)
                    .animateOpacity()
                    .sprite(SpriteBundle.DOT_YELLOW));
            engine.environment().remove(enemy);
        }

    }
}
