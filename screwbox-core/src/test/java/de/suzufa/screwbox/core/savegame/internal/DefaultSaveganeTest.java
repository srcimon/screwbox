package de.suzufa.screwbox.core.savegame.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.scenes.Scenes;
import de.suzufa.screwbox.core.scenes.internal.GameScene;

@ExtendWith(MockitoExtension.class)
class DefaultSaveganeTest {

    private static final Path SAVEGAME = Path.of("mysave.sav");

    @InjectMocks
    DefaultSavegame savegame;

    @Mock
    Scenes scenes;

    @Captor
    ArgumentCaptor<List<Entity>> entitiesCaptor;

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

    @Test
    void exists_doesntExist_false() {
        boolean exists = savegame.exists("unknown");

        assertThat(exists).isFalse();
    }

    @Test
    void exists_exists_true() {
        var entities = mock(Entities.class);
        when(scenes.entitiesOf(GameScene.class)).thenReturn(entities);
        doReturn(GameScene.class).when(scenes).activeScene();

        savegame.create("mysave.sav");

        boolean exists = savegame.exists("mysave.sav");

        assertThat(exists).isTrue();
    }

    @Test
    void load_nameNull_throwsException() {
        assertThatThrownBy(() -> savegame.load(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void load_sceneNull_throwsException() {
        assertThatThrownBy(() -> savegame.load("mysave.sav", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("scene must not be null");
    }

    @Test
    void load_doesntExist_throwsException() {
        var entities = mock(Entities.class);
        when(scenes.entitiesOf(GameScene.class)).thenReturn(entities);

        assertThatThrownBy(() -> savegame.load("not-there.sav", GameScene.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("could not load entities");
    }

    @Test
    void load_saveExists_populatesEntities() {
        var entities = mock(Entities.class);
        when(scenes.entitiesOf(GameScene.class)).thenReturn(entities);
        when(entities.allEntities()).thenReturn(List.of(new Entity(1), new Entity(2)));
        doReturn(GameScene.class).when(scenes).activeScene();
        savegame.create("mysave.sav");

        savegame.load("mysave.sav");

        verify(entities).clearEntities();
        verify(entities).add(entitiesCaptor.capture());

        assertThat(entitiesCaptor.getValue())
                .anyMatch(e -> e.id().get() == 1)
                .anyMatch(e -> e.id().get() == 2)
                .hasSize(2);
    }

    @AfterEach
    void afterEach() throws IOException {
        if (Files.exists(SAVEGAME)) {
            Files.delete(SAVEGAME);
        }
    }
}
