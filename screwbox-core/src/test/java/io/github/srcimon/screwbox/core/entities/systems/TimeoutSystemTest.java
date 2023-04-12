package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.TimeoutComponent;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EntitiesExtension.class)
class TimeoutSystemTest {

    private static final Time NOW = Time.now();
    private static final Time LATER = NOW.plusSeconds(1);

    @Test
    void update_removesTimedOutComponents(DefaultEntities entities, Loop loop) {
        when(loop.lastUpdate()).thenReturn(LATER);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(NOW));
        entities
                .add(timedOutEntity)
                .add(new TimeoutSystem());

        entities.update();

        assertThat(entities.entityCount()).isZero();
    }

    @Test
    void update_dosntTouchNonTimedOutComponents(DefaultEntities entities, Loop loop) {
        when(loop.lastUpdate()).thenReturn(NOW);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(LATER));
        entities
                .add(timedOutEntity)
                .add(new TimeoutSystem());

        entities.update();

        assertThat(entities.entityCount()).isEqualTo(1);
    }
}
