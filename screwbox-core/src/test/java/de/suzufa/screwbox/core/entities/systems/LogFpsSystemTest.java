package de.suzufa.screwbox.core.entities.systems;

import static de.suzufa.screwbox.core.Time.now;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.loop.Loop;
import de.suzufa.screwbox.core.test.EntitiesExtension;

@ExtendWith(EntitiesExtension.class)
class LogFpsSystemTest {

    @Test
    void update_logsCurrentFps(DefaultEntities entities, Loop loop, Log log) {
        when(loop.fps()).thenReturn(50, 30, 10);
        when(loop.lastUpdate()).thenReturn(
                now().plusSeconds(-10), // no logging yet
                now().plusSeconds(-5), // no logging yet
                now().plusSeconds(20)); // log now

        entities.add(new LogFpsSystem());

        entities.updateTimes(3);

        verify(log).debug("current fps 30");
    }
}
