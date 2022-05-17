package de.suzufa.screwbox.core.loop.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultGameLoopTest {

    DefaultGameLoop loop;
    DefaultMetrics metrics;

    @BeforeEach
    void beforeEach() {
        metrics = new DefaultMetrics();
    }

    @Test
    void start_stopAfterFirstIteration_tracksMetrics() {
        loop = new DefaultGameLoop(metrics, () -> loop.stop());

        loop.start();

        assertThat(loop.metrics().frameNumber()).isEqualTo(1);
    }

    @Test
    void stop_notStarted_throwsException() {
        loop = new DefaultGameLoop(metrics);

        assertThatThrownBy(() -> loop.stop())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("game loop hast not been started yet");
    }

    @Test
    void start_alreadyStarted_throwsException() {
        loop = new DefaultGameLoop(metrics, () -> loop.start());

        assertThatThrownBy(() -> loop.start())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("game loop already started");
    }

    @Test
    void setTargetFps_targetFpsValid_setsTargetFps() {
        loop = new DefaultGameLoop(metrics);
        loop.setTargetFps(72);

        assertThat(loop.targetFps()).isEqualTo(72);
    }

    @Test
    void setTargetFps_targetFpsBelowZero_throwsExeption() {
        loop = new DefaultGameLoop(metrics);

        assertThatThrownBy(() -> loop.setTargetFps(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("target fps must have a positive value");
    }
}
