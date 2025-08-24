package dev.screwbox.core.scenes.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.scenes.DefaultScene;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.scenes.SceneTransition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings
class DefaultScenesTest {

    @Mock
    Engine engine;

    @Mock
    Canvas canvas;

    ExecutorService executor;

    DefaultScenes scenes;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        scenes = new DefaultScenes(engine, canvas, executor);
        scenes.setLoadingScene(environment -> {
        });
    }

    @Test
    void sceneCount_isCountOfScenes() {
        scenes.add(mock(Scene.class));

        assertThat(scenes.sceneCount()).isEqualTo(2);
    }

    @Test
    void switchTo_sceneDoesntExist_throwsException() {
        assertThatThrownBy(() -> scenes.switchTo(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scene doesn't exist: class dev.screwbox.core.scenes.internal.GameScene");
    }

    @Test
    void remove_sceneDoesntExist_throwsException() {
        assertThatThrownBy(() -> scenes.remove(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scene doesn't exist: class dev.screwbox.core.scenes.internal.GameScene");
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
    void remove_sceneIsPresentAndInactive_removesScene() {
        scenes.add(new GameScene());

        scenes.remove(GameScene.class);

        assertThat(scenes.exists(GameScene.class)).isFalse();
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

        assertThat(scenes.exists(GameScene.class)).isTrue();
    }

    @Test
    void addOrReplace_present_replaced() {
        GameScene newScene = new GameScene();
        scenes.add(new GameScene());

        scenes.addOrReplace(newScene);

        assertThat(scenes.exists(GameScene.class)).isTrue();
    }

    @Test
    void exits_sceneExists_isTrue() {
        scenes.add(new GameScene());

        assertThat(scenes.exists(GameScene.class)).isTrue();
    }

    @Test
    void exits_sceneNotPresent_isFalse() {
        assertThat(scenes.exists(GameScene.class)).isFalse();
    }

    @Test
    void update_withSceneChange_initializesAndEntersScene() {
        scenes.update();
        var firstScene = mock(Scene.class);
        scenes.add(firstScene);

        scenes.switchTo(firstScene.getClass());

        scenes.update();

        shutdown(executor);
        verify(firstScene).populate(any());
        verify(firstScene).onEnter(engine);
    }

    @Test
    void isTransitioning_noTransitionInProgress_isFalse() {
        assertThat(scenes.isTransitioning()).isFalse();
    }

    @Test
    void isTransitioning_transitionInProgress_isTrue() {
        scenes.add(new GameScene());

        scenes.switchTo(GameScene.class, SceneTransition.custom());

        assertThat(scenes.isTransitioning()).isTrue();
    }

    @Test
    void add_sceneAlreadyPresent_throwsException() {
        scenes.add(new GameScene());
        GameScene gameScene = new GameScene();

        assertThatThrownBy(() -> scenes.add(gameScene))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scene is already present: class dev.screwbox.core.scenes.internal.GameScene");
    }

    @Test
    void resetActiveScene_repopulatesActiveScene() {
        Scene mockScene = mock(Scene.class);
        scenes.add(mockScene);
        scenes.switchTo(mockScene.getClass());
        scenes.update();

        scenes.resetActiveScene();

        shutdown(executor);
        verify(mockScene, times(2)).populate(any()); // first time when added, second time on reset
        assertThat(scenes.activeScene()).isEqualTo(mockScene.getClass());
        assertThat(scenes.exists(mockScene.getClass())).isTrue();
    }

    @AfterEach
    void afterEach() {
        shutdown(executor);
    }
}
