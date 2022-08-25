package de.suzufa.screwbox.core.entityengine.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TimeoutComponent;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.test.extensions.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class TimeoutSystemTest {

    private static final Time NOW = Time.now();
    private static final Time LATER = NOW.plusSeconds(1);

    @Test
    void update_removesTimedOutComponents(DefaultEntityEngine entityEngine, GameLoop loop) {
        when(loop.lastUpdate()).thenReturn(LATER);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(NOW));
        entityEngine.add(timedOutEntity);

        entityEngine.add(new TimeoutSystem());

        entityEngine.update();

        assertThat(entityEngine.entityCount()).isZero();
    }

    @Test
    void update_dosntTouchNonTimedOutComponents(DefaultEntityEngine entityEngine, GameLoop loop) {
        when(loop.lastUpdate()).thenReturn(NOW);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(LATER));
        entityEngine.add(timedOutEntity);

        entityEngine.add(new TimeoutSystem());

        entityEngine.update();

        assertThat(entityEngine.entityCount()).isEqualTo(1);
    }
}
