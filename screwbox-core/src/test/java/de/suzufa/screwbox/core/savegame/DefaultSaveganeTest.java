package de.suzufa.screwbox.core.savegame;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.savegame.internal.DefaultSavegame;
import de.suzufa.screwbox.core.scenes.Scenes;

@ExtendWith(MockitoExtension.class)
class DefaultSaveganeTest {

    private static final Path SAVEGAME = Path.of("mysave.sav");

    @InjectMocks
    DefaultSavegame savegame;

    @Mock
    Scenes scenes;

    @Test
    void create_nameNull_throwsException() {
        assertThatThrownBy(() -> savegame.create(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void create_namePresent_createsSaveFile() {
        var entities = Mockito.mock(Entities.class);
        Mockito.when(scenes.entitiesOf(Mockito.any())).thenReturn(entities);
        savegame.create("mysave.sav");

        assertThat(Files.exists(SAVEGAME)).isTrue();
    }

    @AfterEach
    void afterEach() throws IOException {
        if (Files.exists(SAVEGAME)) {
            Files.delete(SAVEGAME);
        }
    }
}
