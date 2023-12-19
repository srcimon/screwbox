package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Environment;

/**
 * Updates the state {@link TweenComponent}s in the {@link Environment}. Required to enable all tweening mechanisms.
 */
public class TweenSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class);

    @Override
    public void update(final Engine engine) {
        final var now = engine.loop().lastUpdate();
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var tween = tweenEntity.get(TweenComponent.class);
            final var elapsedDuration = Duration.between(now, tween.startTime);
            tween.progress = tween.reverse
                    ? Percent.of(1.0 - 1.0 * elapsedDuration.nanos() / tween.duration.nanos())
                    : Percent.of(1.0 * elapsedDuration.nanos() / tween.duration.nanos());
            tween.progressValue = tween.mode.applyOn(tween.progress);

            if (tween.reverse && tween.progress.isMinValue() || !tween.reverse && tween.progress.isMaxValue()) {
                if (tween.isLooped) {
                    tween.startTime = now;
                    tween.reverse = !tween.reverse;
                } else {
                    tweenEntity.remove(TweenComponent.class);
                }
            }
        }
    }
}
