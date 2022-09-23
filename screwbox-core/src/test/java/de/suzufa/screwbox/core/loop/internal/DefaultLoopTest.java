package de.suzufa.screwbox.core.loop.internal;

import static de.suzufa.screwbox.core.Duration.none;
import static de.suzufa.screwbox.core.Time.unset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;

class DefaultLoopTest {

    DefaultLoop loop;
    List<Updatable> updatables;

    @BeforeEach
    void beforeEach() {
        updatables = new ArrayList<>();
        loop = new DefaultLoop(updatables);
    }

    @Test
    void runningTime_notStarted_returnsNone() {
        assertThat(loop.runningTime()).isEqualTo(none());
    }

    @Test
    void runningTime_started_returnsDuration() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        assertThat(loop.runningTime()).isNotEqualTo(none());
    }

    @Test
    void runningTime_startedAgain_returnsDurationSinceSecondStart() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        Time time = Time.now();

        loop.start();

        assertThat(loop.runningTime()).isNotEqualTo(none());
        assertThat(loop.runningTime().isLessThan(Duration.since(time))).isTrue();
    }

    @Test
    void startTime_notStarted_returnsUnset() {
        assertThat(loop.startTime()).isEqualTo(unset());
    }

    @Test
    void startTime_started_returnsStartTime() {
        updatables.add(stopAfterOneFrameUpdatable());

        Time before = Time.now();
        loop.start();
        Time after = Time.now();
        assertThat(loop.startTime().isAfter(before)).isTrue();
        assertThat(loop.startTime().isBefore(after)).isTrue();
    }

    @Test
    void startTime_startedAgain_returnsSecondStartTime() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        Time before = Time.now();
        loop.start();
        Time after = Time.now();

        assertThat(loop.startTime().isAfter(before)).isTrue();
        assertThat(loop.startTime().isBefore(after)).isTrue();
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
    void setTargetFps_targetFpsBelowMinFps_throwsExeption() {
        assertThatThrownBy(() -> loop.setTargetFps(80))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("target fps must be at least 120");
    }

    private Updatable stopAfterOneFrameUpdatable() {
        return () -> loop.stop();
    }
}
