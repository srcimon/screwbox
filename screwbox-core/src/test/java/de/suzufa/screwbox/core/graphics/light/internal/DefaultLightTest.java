package de.suzufa.screwbox.core.graphics.light.internal;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.internal.DefaultLight;
import de.suzufa.screwbox.core.graphics.internal.DefaultWorld;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultLightTest {

    ExecutorService executor;

    @Mock
    Window window;

    @Mock
    DefaultWorld world;

    GraphicsConfiguration configuration;

    DefaultLight light;

    @BeforeEach
    void beforeEach() {
        when(window.size()).thenReturn(Dimension.of(640, 480));
        configuration = new GraphicsConfiguration();
        executor = Executors.newSingleThreadExecutor();
        light = new DefaultLight(window, world, configuration, executor);
    }

    @Test
    void updateObstacles_obstaclesNull_throwsException() {
        assertThatThrownBy(() -> light.updateObstacles(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("obstacles must not be null");
    }

    @Test
    void updateObstacles_obstaclesPresent_updatesObstacles() {
        Bounds obstacle = $$(0, 10, 20, 20);

        light.updateObstacles(List.of(obstacle));

        assertThat(light.obstacles()).containsExactly(obstacle);
    }

    @Test
    void seal_alreadySealed_throwsException() {
        light.seal();

        assertThatThrownBy(() -> light.seal())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("lightmap has already been sealed");
    }

    @AfterEach
    void afterEach() {
        shutdown(executor);
    }

}
