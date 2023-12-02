package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.internal.DefaultEntities;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(EntitiesExtension.class)
class QuitOnKeyPressSystemTest {

    @Test
    void update_quitKeyNotPressed_doesntStopEngine(Engine engine, DefaultEntities entities) {
        entities.addSystem(new QuitOnKeyPressSystem(Key.SPACE));
        entities.update();

        verify(engine, never()).stop();
    }

    @Test
    void update_quitKeyPressed_stopsEngine(Engine engine, DefaultEntities entities, Keyboard keyboard) {
        when(keyboard.justPressed(Key.SPACE)).thenReturn(true);

        entities.addSystem(new QuitOnKeyPressSystem(Key.SPACE));
        entities.update();

        verify(engine).stop();
    }
}
