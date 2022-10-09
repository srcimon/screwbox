package de.suzufa.screwbox.core.savegame.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.scenes.Scenes;
import de.suzufa.screwbox.core.scenes.internal.GameScene;

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
    void create_sceneNull_throwsException() {
        assertThatThrownBy(() -> savegame.create("mysave.sav", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("scene must not be null");
    }

    @Test
    void create_namePresent_createsSaveFile() {
        var entities = mock(Entities.class);
        when(scenes.entitiesOf(GameScene.class)).thenReturn(entities);
        doReturn(GameScene.class).when(scenes).activeScene();

        savegame.create("mysave.sav");

        assertThat(Files.exists(SAVEGAME)).isTrue();
    }

    @Test
    void create_sceneClassGiven_createsSaveFile() {
        var entities = mock(Entities.class);
        when(scenes.entitiesOf(GameScene.class)).thenReturn(entities);

        savegame.create("mysave.sav", GameScene.class);

        assertThat(Files.exists(SAVEGAME)).isTrue();
    }

    @Test
    void exists_nameNull_throwsException() {
        assertThatThrownBy(() -> savegame.exists(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @AfterEach
    void afterEach() throws IOException {
        if (Files.exists(SAVEGAME)) {
            Files.delete(SAVEGAME);
        }
    }
}
