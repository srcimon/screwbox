package de.suzufa.screwbox.core.entityengine.systems;

import static de.suzufa.screwbox.core.Time.now;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.loop.Metrics;
import de.suzufa.screwbox.test.extensions.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class LogFpsSystemTest {

    @Test
    void update_updatesPositionOfPhysicItems(DefaultEntityEngine entityEngine, Metrics metrics, Log log) {
        when(metrics.framesPerSecond()).thenReturn(50, 30, 10);
        when(metrics.lastUpdate()).thenReturn(
                now().plusSeconds(-10), // no logging yet
                now().plusSeconds(-5), // no logging yet
                now().plusSeconds(20)); // log now

        entityEngine.add(new LogFpsSystem());

        entityEngine.updateTimes(3);

        verify(log).debug("current fps 30");
    }
}
