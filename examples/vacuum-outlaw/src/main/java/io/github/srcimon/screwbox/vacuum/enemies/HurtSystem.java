package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.CameraShakeOptions.lastingForDuration;

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
