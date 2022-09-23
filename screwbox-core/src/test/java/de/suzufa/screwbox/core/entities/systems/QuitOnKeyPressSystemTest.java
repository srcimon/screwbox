package de.suzufa.screwbox.core.entities.systems;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.core.test.EntitiesExtension;

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
