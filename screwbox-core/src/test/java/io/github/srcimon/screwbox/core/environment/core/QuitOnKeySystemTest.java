package io.github.srcimon.screwbox.core.environment.core;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(EnvironmentExtension.class)
class QuitOnKeySystemTest {

    @Test
    void update_quitKeyNotPressed_doesntStopEngine(Engine engine, DefaultEnvironment environment) {
        environment.addSystem(new QuitOnKeySystem(Key.SPACE));
        environment.update();

        verify(engine, never()).stop();
    }

    @Test
    void update_quitKeyPressed_stopsEngine(Engine engine, DefaultEnvironment environment, Keyboard keyboard) {
        when(keyboard.isPressed(Key.SPACE)).thenReturn(true);

        environment.addSystem(new QuitOnKeySystem(Key.SPACE));
        environment.update();

        verify(engine).stop();
    }
}
