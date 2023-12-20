package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class TweenDestroySystemTest {

    @Test
    void update_removesEntitiesWithElapsedTweens(DefaultEnvironment environment, Loop loop) {
        when(loop.lastUpdate()).thenReturn(Time.now().plusSeconds(-1));

        environment
                .addEntity(1, new TweenComponent(ofMillis(200)), new TweenDestroyComponent())
                .addEntity(2, new TweenComponent(ofSeconds(40)), new TweenDestroyComponent())
                .addSystem(new TweenDestroySystem())
                .addSystem(new TweenSystem());

        environment.update(); // entity 1 loeses is TweenComponent
        environment.update(); // entity 1 gets removed

        assertThat(environment.fetchById(1)).isEmpty();
        assertThat(environment.fetchById(2)).isNotEmpty();
    }
}
