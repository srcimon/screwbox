package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.tweening.TimeoutComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.tweening.TimeoutSystem;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class TimeoutSystemTest {

    private static final Time NOW = Time.now();
    private static final Time LATER = NOW.plusSeconds(1);

    @Test
    void update_removesTimedOutComponents(DefaultEnvironment environment, Loop loop) {
        when(loop.lastUpdate()).thenReturn(LATER);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(NOW));
        environment
                .addEntity(timedOutEntity)
                .addSystem(new TimeoutSystem());

        environment.update();

        assertThat(environment.entityCount()).isZero();
    }

    @Test
    void update_dosntTouchNonTimedOutComponents(DefaultEnvironment environment, Loop loop) {
        when(loop.lastUpdate()).thenReturn(NOW);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(LATER));
        environment
                .addEntity(timedOutEntity)
                .addSystem(new TimeoutSystem());

        environment.update();

        assertThat(environment.entityCount()).isEqualTo(1);
    }
}
