package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;

/**
 * Updates the state {@link TweenComponent}s in the {@link Environment}. Required to enable all tweening mechanisms.
 */
public class TweenSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class);

    @Override
    public void update(final Engine engine) {
        final Time now = engine.loop().time();
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var tween = tweenEntity.get(TweenComponent.class);
            tween.progress = calculateProgressOfTween(now, tween);
            tween.value = tween.ease.applyOn(tween.progress);

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

    private static Percent calculateProgressOfTween(final Time now, final TweenComponent tween) {
        final var elapsedDuration = Duration.between(now, tween.startTime);
        return tween.reverse
                ? Percent.of(1.0 - 1.0 * elapsedDuration.nanos() / tween.duration.nanos())
                : Percent.of(1.0 * elapsedDuration.nanos() / tween.duration.nanos());
    }

    private static boolean tweenHasReachedEnd(final TweenComponent tween) {
        return tween.reverse && tween.progress.isZero() || !tween.reverse && tween.progress.isMax();
    }
}
