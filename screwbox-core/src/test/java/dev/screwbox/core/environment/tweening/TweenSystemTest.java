package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class TweenSystemTest {

    @Test
    void update_updatesProgression(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now());

        Entity smoke = new Entity().add(new TweenComponent(ofMillis(200)));

        environment
                .addEntity(smoke)
                .addSystem(new TweenSystem());

        environment.update();

        assertThat(smoke.get(TweenComponent.class).progress.isZero()).isFalse();
    }

    @Test
    void update_progressionMax_entityRemoved(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now().addSeconds(4));

        Entity smoke = new Entity().add(new TweenComponent(ofMillis(200)));

        environment
                .addEntity(smoke)
                .addSystem(new TweenSystem());

        environment.update();

        assertThat(environment.entities()).isEmpty();
    }

    @Test
    void update_loopedInterval_entityNotRemoved(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now().addSeconds(4));

        Entity smoke = new Entity().add(new TweenComponent(ofMillis(200), Ease.LINEAR_IN, true));

        environment
                .addEntity(smoke)
                .addSystem(new TweenSystem());

        environment.update();

        assertThat(environment.entities()).contains(smoke);
    }
}