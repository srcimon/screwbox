package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.TimeoutComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.test.EntitiesExtension;

@ExtendWith(EntitiesExtension.class)
class TimeoutSystemTest {

    private static final Time NOW = Time.now();
    private static final Time LATER = NOW.plusSeconds(1);

    @Test
    void update_removesTimedOutComponents(DefaultEntities entities, GameLoop loop) {
        when(loop.lastUpdate()).thenReturn(LATER);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(NOW));
        entities
                .add(timedOutEntity)
                .add(new TimeoutSystem());

        entities.update();

        assertThat(entities.entityCount()).isZero();
    }

    @Test
    void update_dosntTouchNonTimedOutComponents(DefaultEntities entities, GameLoop loop) {
        when(loop.lastUpdate()).thenReturn(NOW);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(LATER));
        entities
                .add(timedOutEntity)
                .add(new TimeoutSystem());

        entities.update();

        assertThat(entities.entityCount()).isEqualTo(1);
    }
}
