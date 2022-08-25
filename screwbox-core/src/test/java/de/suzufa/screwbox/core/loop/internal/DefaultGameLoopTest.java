package de.suzufa.screwbox.core.loop.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultGameLoopTest {

    DefaultGameLoop loop;
    List<Updatable> updatables;

    @BeforeEach
    void beforeEach() {
        updatables = new ArrayList<>();
        loop = new DefaultGameLoop(updatables);
    }

    @Test
    void delta_afterOneIteration_returnsCorrectValue() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        assertThat(loop.delta()).isPositive().isEqualTo(1.0 / loop.fps(), within(0.2));
    }

    @Test
    void frameNumber_afterOneIteration_returnsOne() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        assertThat(loop.frameNumber()).isEqualTo(1);
    }

    @Test
    void stop_notStarted_throwsException() {
        assertThatThrownBy(() -> loop.stop())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("game loop hast not been started yet");
    }

    @Test
    void start_alreadyStarted_throwsException() {
        updatables.add(() -> loop.start());

        assertThatThrownBy(() -> loop.start())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("game loop already started");
    }

    @Test
    void setTargetFps_targetFpsValid_setsTargetFps() {
        loop.setTargetFps(172);

        assertThat(loop.targetFps()).isEqualTo(172);
    }

    @Test
    void setTargetFps_targetFpsBelowDefaultFps_throwsExeption() {
        assertThatThrownBy(() -> loop.setTargetFps(80))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("target fps must be at least 120");
    }

    private Updatable stopAfterOneFrameUpdatable() {
        return () -> loop.stop();
    }
}
