package dev.screwbox.core.environment.core;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.log.Log;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Time.now;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class LogFpsSystemTest {

    @Test
    void update_updatesPositionOfPhysicItems(DefaultEnvironment environment, Loop loop, Log log) {
        when(loop.fps()).thenReturn(50, 30, 10);
        when(loop.time()).thenReturn(
                now().addSeconds(-10), // no logging yet
                now().addSeconds(-5), // no logging yet
                now().addSeconds(20)); // log now

        environment.addSystem(new LogFpsSystem());

        environment.updateTimes(3);

        verify(log).debug("current fps 30");
    }
}
