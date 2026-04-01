package dev.screwbox.core.environment.core;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.log.Log;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.scenes.Scenes;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.screwbox.core.Time.now;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class LogFpsSystemTest {

    @Test
    void update_updatesPositionOfPhysicItems(DefaultEnvironment environment, Loop loop, Log log, Scenes scenes) {
        when(loop.fps()).thenReturn(50, 30, 10);
        when(scenes.switchTime()).thenReturn(Time.now().addSeconds(-120));

        when(loop.time()).thenReturn(
            now().addSeconds(4),
            now().addSeconds(5),
            now().addSeconds(8));

        environment.addSystem(new LogFpsSystem());

        environment.updateTimes(3);

        verify(log).debug("current fps {}", 50);
        verify(log).debug("current fps {}", 30);
    }
}
