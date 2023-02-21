package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.internal.DefaultEntities;
import io.github.simonbas.screwbox.core.keyboard.Key;
import io.github.simonbas.screwbox.core.keyboard.Keyboard;
import io.github.simonbas.screwbox.core.test.EntitiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(EntitiesExtension.class)
class QuitOnKeyPressSystemTest {

    @Test
    void update_quitKeyNotPressed_doesntStopEngine(Engine engine, DefaultEntities entities) {
        entities.add(new QuitOnKeyPressSystem(Key.SPACE));
        entities.update();

        verify(engine, never()).stop();
    }

    @Test
    void update_quitKeyPressed_stopsEngine(Engine engine, DefaultEntities entities, Keyboard keyboard) {
        when(keyboard.justPressed(Key.SPACE)).thenReturn(true);

        entities.add(new QuitOnKeyPressSystem(Key.SPACE));
        entities.update();

        verify(engine).stop();
    }
}
