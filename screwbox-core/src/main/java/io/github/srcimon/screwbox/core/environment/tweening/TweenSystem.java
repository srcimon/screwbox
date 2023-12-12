package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

//TODO: javadoc and tests
public class TweenSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenStateComponent.class);

    @Override
    public void update(final Engine engine) {
        final var now = engine.loop().lastUpdate();
        for (final var tween : engine.environment().fetchAll(TWEENS)) {
            final var state = tween.get(TweenStateComponent.class);
            final var elapsedDuration = Duration.between(now, state.startTime);
            state.progress = state.reverse
                    ? Percent.of(1.0 - 1.0 * elapsedDuration.nanos() / state.duration.nanos())
                    : Percent.of(1.0 * elapsedDuration.nanos() / state.duration.nanos());
            if (state.reverse && state.progress.isMinValue() || !state.reverse && state.progress.isMaxValue()) {
                if (state.isLooped) {
                    state.startTime = now;
                    state.reverse = !state.reverse;
                } else {
                    engine.environment().remove(tween);
                }
            }
        }
    }
}
