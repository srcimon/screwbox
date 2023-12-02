package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.srcimon.screwbox.core.Time.now;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EntitiesExtension.class)
class LogFpsSystemTest {

    @Test
    void update_updatesPositionOfPhysicItems(DefaultEntities entities, Loop loop, Log log) {
        when(loop.fps()).thenReturn(50, 30, 10);
        when(loop.lastUpdate()).thenReturn(
                now().plusSeconds(-10), // no logging yet
                now().plusSeconds(-5), // no logging yet
                now().plusSeconds(20)); // log now

        entities.addSystem(new LogFpsSystem());

        entities.updateTimes(3);

        verify(log).debug("current fps 30");
    }
}
