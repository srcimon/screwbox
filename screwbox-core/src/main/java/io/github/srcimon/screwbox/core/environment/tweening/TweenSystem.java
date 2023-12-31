package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
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
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var tween = tweenEntity.get(TweenComponent.class);
            Time now = engine.loop().lastUpdate();
            tween.progress = calculateProgressOfTween(now, tween);
            tween.value = tween.mode.applyOn(tween.progress);

            if (tweenHasReachedEnd(tween)) {
                if (tween.isLooped) {
                    tween.startTime = now;
                    if (tween.usePingPong) {
                        tween.reverse = !tween.reverse;
                    }
                } else {
                    tweenEntity.remove(TweenComponent.class);
                }
            }
        }
    }

    private Percent calculateProgressOfTween(final Time now, final TweenComponent tween) {
        final var elapsedDuration = Duration.between(now, tween.startTime);
        return tween.reverse
                ? Percent.of(1.0 - 1.0 * elapsedDuration.nanos() / tween.duration.nanos())
                : Percent.of(1.0 * elapsedDuration.nanos() / tween.duration.nanos());
    }

    private boolean tweenHasReachedEnd(final TweenComponent tween) {
        return tween.reverse && tween.progress.isZero() || !tween.reverse && tween.progress.isMax();
    }
}
