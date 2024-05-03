package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.scenes.DefaultScene;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.ui.Ui;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultScenesTest {

    @Mock
    Engine engine;

    @Mock
    Ui ui;

    @Mock
    Screen screen;

    ExecutorService executor;

    DefaultScenes scenes;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        scenes = new DefaultScenes(engine, screen, executor);
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
                .hasMessage("scene doesn't exist: class io.github.srcimon.screwbox.core.scenes.internal.GameScene");
    }

    @Test
    void remove_sceneDoesntExist_throwsException() {
        assertThatThrownBy(() -> scenes.remove(GameScene.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scene doesn't exist: class io.github.srcimon.screwbox.core.scenes.internal.GameScene");
    }

    @Test
    void remove_isActiveScene_throwsException() {
        when(engine.isWarmedUp()).thenReturn(true);
        scenes.add(new GameScene());
        scenes.switchTo(GameScene.class);
        scenes.update();

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
        when(engine.isWarmedUp()).thenReturn(true);
        var firstScene = mock(Scene.class);
        scenes.add(firstScene);

        scenes.switchTo(firstScene.getClass());

        scenes.update();

        shutdown(executor);
        verify(firstScene).populate(any());
        verify(firstScene).onEnter(engine);
        verify(screen).takeScreenshot();
    }

    @Test
    void previousSceneScreenshot_noSceneChange_isEmpty() {
        assertThat(scenes.previousSceneScreenshot()).isEmpty();
    }

    @Test
    void previousSceneScreenshot_sceneChanged_containsScreenshot() {
        when(screen.takeScreenshot()).thenReturn(Sprite.invisible());
        var firstScene = mock(Scene.class);
        scenes.add(firstScene);

        scenes.switchTo(firstScene.getClass());
        scenes.update();

        assertThat(scenes.previousSceneScreenshot()).contains(Sprite.invisible());
    }

    @Test
    void isTransitioning_noTransitionInProgress_isFalse() {
        assertThat(scenes.isTransitioning()).isFalse();
    }

    @Test
    void isTransitioning_transitionInProgress_isTrue() {
        scenes.add(new GameScene());

        scenes.switchTo(GameScene.class, SceneTransition.noExtro());

        assertThat(scenes.isTransitioning()).isTrue();
    }

    @AfterEach
    void afterEach() {
        shutdown(executor);
    }
}
