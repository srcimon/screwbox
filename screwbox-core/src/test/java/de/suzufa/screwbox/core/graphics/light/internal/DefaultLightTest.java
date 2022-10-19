package de.suzufa.screwbox.core.graphics.light.internal;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    DefaultLight light;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        light = new DefaultLight(window, world, executor);
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

    @AfterEach
    void afterEach() {
        shutdown(executor);
    }

}
