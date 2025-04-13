package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class TweenDestroySystemTest {

    @Test
    void update_removesEntitiesWithElapsedTweens(DefaultEnvironment environment, Loop loop) {
        when(loop.time()).thenReturn(Time.now().addSeconds(-1));

        environment
                .addEntity(1, new TweenComponent(ofMillis(200)), new TweenDestroyComponent())
                .addEntity(2, new TweenComponent(ofSeconds(40)), new TweenDestroyComponent())
                .addSystem(new TweenDestroySystem())
                .addSystem(new TweenSystem());

        environment.update(); // entity 1 loses is TweenComponent
        environment.update(); // entity 1 gets removed

        assertThat(environment.tryFetchById(1)).isEmpty();
        assertThat(environment.tryFetchById(2)).isNotEmpty();
    }
}
