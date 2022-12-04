package de.suzufa.screwbox.core.scenes.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.scenes.DefaultScene;
import de.suzufa.screwbox.core.scenes.Scene;

@ExtendWith(MockitoExtension.class)
class DefaultScenesTest {

    @Mock
    private Engine engine;

    @InjectMocks
    private DefaultScenes scenes;

    @Test
    void sceneCount_isCountOfScenes() {
        scenes.add(mock(Scene.class));

        assertThat(scenes.sceneCount()).isEqualTo(2);
    }

    @Test
    void switchTo_sceneDoesntExist_throwsException() {
        assertThatThrownBy(() -> scenes.switchTo(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("missing scene: class de.suzufa.screwbox.core.scenes.internal.GameScene");
    }

    @Test
    void remove_sceneDoesntExist_throwsException() {
        assertThatThrownBy(() -> scenes.remove(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scene doesn't exist: class de.suzufa.screwbox.core.scenes.internal.GameScene");
    }

    @Test
    void activeScene_noSceneAdded_isDefaultScene() {
        assertThat(scenes.activeScene()).isEqualTo(DefaultScene.class);
    }

    @Test
    void isActive_noSceneAdded_isFalse() {
        assertThat(scenes.isActive(GameScene.class)).isFalse();
    }

}
