//package io.github.srcimon.screwbox.core.savegame.internal;
//
//import io.github.srcimon.screwbox.core.environment.Environment;
//import io.github.srcimon.screwbox.core.environment.Entity;
//import io.github.srcimon.screwbox.core.scenes.Scenes;
//import io.github.srcimon.screwbox.core.scenes.internal.GameScene;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class DefaultSaveganeTest {
//
//    private static final String SAVEGAME_NAME = "mysave.sav";
//
//    private static final Path SAVEGAME = Path.of(SAVEGAME_NAME);
//
//    @InjectMocks
//    DefaultSavegame savegame;
//
//    @Mock
//    Scenes scenes;
//
//    @Captor
//    ArgumentCaptor<List<Entity>> entitiesCaptor;
//
//
//    @Test
//    void load_saveExists_populatesEntities() {
//        var entities = mock(Environment.class);
//        when(scenes.environmentOf(GameScene.class)).thenReturn(entities);
//        when(entities.entities()).thenReturn(List.of(new Entity(1), new Entity(2)));
//        doReturn(GameScene.class).when(scenes).activeScene();
//        savegame.create(SAVEGAME_NAME);
//
//        savegame.load(SAVEGAME_NAME);
//
//        verify(entities).clearEntities();
//        verify(entities).addEntities(entitiesCaptor.capture());
//
//        assertThat(entitiesCaptor.getValue())
//                .anyMatch(e -> e.id().get() == 1)
//                .anyMatch(e -> e.id().get() == 2)
//                .hasSize(2);
//    }
//
//    @Test
//    void delete_nameNull_throwsException() {
//        assertThatThrownBy(() -> savegame.delete(null))
//                .isInstanceOf(NullPointerException.class)
//                .hasMessage("name must not be null");
//    }
//
//    @Test
//    void delete_savegameDoesntExist_throwsException() {
//        assertThatThrownBy(() -> savegame.delete("not-there.sav"))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessage("could not delete savegame: not-there.sav");
//    }
//
//    @Test
//    void delete_savegameExists_deletesSAve() {
//        var entities = mock(Environment.class);
//        when(scenes.environmentOf(GameScene.class)).thenReturn(entities);
//        doReturn(GameScene.class).when(scenes).activeScene();
//        savegame.create(SAVEGAME_NAME);
//
//        savegame.delete(SAVEGAME_NAME);
//
//        assertThat(savegame.exists(SAVEGAME_NAME)).isFalse();
//    }
//
//    @AfterEach
//    void afterEach() throws IOException {
//        if (Files.exists(SAVEGAME)) {
//            Files.delete(SAVEGAME);
//        }
//    }
//}