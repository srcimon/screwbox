package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

//TODO: javadoc and tests
public class TweenSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenState.class);

    @Override
    public void update(final Engine engine) {
        final var now = engine.loop().lastUpdate();
        for (final var tween : engine.environment().fetchAll(TWEENS)) {
            final var state = tween.get(TweenState.class);
            final var elapsedDuration = Duration.between(now, state.startTime);
            state.progress = state.reverse
                    ? Percent.of(1.0 - 1.0 * elapsedDuration.nanos() / state.duration.nanos())
                    : Percent.of(1.0 * elapsedDuration.nanos() / state.duration.nanos());

            if (state.loopCount >= 0) {
                if (state.reverse && state.progress.isMinValue() || !state.reverse && state.progress.isMaxValue()) {
                    state.reverse = !state.reverse;
                    if (state.loopCount <= 1) {
                        tween.remove(TweenState.class);
                    }
                    if (state.loopCount != 0) {
                        state.loopCount--;
                    }
                    state.startTime = now;
                }
            }
        }
    }
}
