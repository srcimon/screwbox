package io.github.simonbas.screwbox.core.scenes.internal;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.scenes.DefaultScene;
import io.github.simonbas.screwbox.core.scenes.Scene;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultScenesTest {

    @Mock
    Engine engine;

    @InjectMocks
    DefaultScenes scenes;

    @Test
    void sceneCount_isCountOfScenes() {
        scenes.add(mock(Scene.class));

        assertThat(scenes.sceneCount()).isEqualTo(2);
    }

    @Test
    void switchTo_sceneDoesntExist_throwsException() {
        assertThatThrownBy(() -> scenes.switchTo(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("missing scene: class io.github.simonbas.screwbox.core.scenes.internal.GameScene");
    }

    @Test
    void remove_sceneDoesntExist_throwsException() {
        assertThatThrownBy(() -> scenes.remove(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scene doesn't exist: class io.github.simonbas.screwbox.core.scenes.internal.GameScene");
    }

    @Test
    void remove_isActiveScene_throwsException() {
        scenes.add(new GameScene());
        scenes.switchTo(GameScene.class);
        scenes.update();

        assertThatThrownBy(() -> scenes.remove(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot remove active scene");
    }

    @Test
    void remove_isNextActiveScene_throwsException() {
        scenes.add(new GameScene());
        scenes.switchTo(GameScene.class);

        assertThatThrownBy(() -> scenes.remove(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot remove active scene");
    }

    @Test
    void activeScene_noSceneAdded_isDefaultScene() {
        assertThat(scenes.activeScene()).isEqualTo(DefaultScene.class);
    }

    @Test
    void isActive_noSceneAdded_isFalse() {
        assertThat(scenes.isActive(GameScene.class)).isFalse();
    }

    @Test
    void addOrReplace_notPresent_onlyAdded() {
        GameScene newScene = new GameScene();

        scenes.addOrReplace(newScene);

        assertThat(scenes.contains(GameScene.class)).isTrue();
    }

    @Test
    void addOrReplace_present_replaced() {
        GameScene newScene = new GameScene();
        scenes.add(new GameScene());

        scenes.addOrReplace(newScene);

        assertThat(scenes.contains(GameScene.class)).isTrue();
    }

    @Test
    void update_withSceneChange_initializesAndEntersScene() {
        var firstScene = mock(Scene.class);
        scenes.add(firstScene);

        scenes.switchTo(firstScene.getClass());

        scenes.update();

        verify(firstScene).initialize(any());
        verify(firstScene).onEnter(engine);
    }
}
