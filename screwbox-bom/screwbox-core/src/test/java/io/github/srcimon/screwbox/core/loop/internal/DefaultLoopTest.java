package io.github.srcimon.screwbox.core.loop.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
        Assertions.assertThat(loop.runningTime()).isEqualTo(Duration.none());
    }

    @Test
    void runningTime_started_returnsDuration() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        Assertions.assertThat(loop.runningTime()).isNotEqualTo(Duration.none());
    }

    @Test
    void runningTime_startedAgain_returnsDurationSinceSecondStart() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        Time time = Time.now();

        loop.start();

        Assertions.assertThat(loop.runningTime()).isNotEqualTo(Duration.none());
        Assertions.assertThat(loop.runningTime().isLessThan(Duration.since(time))).isTrue();
    }

    @Test
    void startTime_notStarted_returnsUnset() {
        Assertions.assertThat(loop.startTime()).isEqualTo(Time.unset());
    }

    @Test
    void startTime_started_returnsStartTime() {
        updatables.add(stopAfterOneFrameUpdatable());

        Time before = Time.now();
        loop.start();
        Time after = Time.now();
        Assertions.assertThat(loop.startTime().isAfter(before)).isTrue();
        Assertions.assertThat(loop.startTime().isBefore(after)).isTrue();
    }

    @Test
    void startTime_startedAgain_returnsSecondStartTime() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        Time before = Time.now();
        loop.start();
        Time after = Time.now();

        Assertions.assertThat(loop.startTime().isAfter(before)).isTrue();
        Assertions.assertThat(loop.startTime().isBefore(after)).isTrue();
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
    void stop_notStarted_ignored() {
        assertThatNoException().isThrownBy(() -> loop.stop());
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

    @Test
    void awaitTermination_threadWasStoppedBecauseOfException_doenstBlockEndlessly() {
        updatables.add(() -> {
            throw new IllegalStateException("exception in loop");

        });

        assertThatThrownBy(loop::start)
                .isInstanceOf(IllegalStateException.class);

        assertThatNoException().isThrownBy(loop::awaitTermination);
    }

    @Test
    void awaitTermination_threadWasStoppedBecauseOfSystem_doenstBlockEndlessly() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        assertThatNoException().isThrownBy(loop::awaitTermination);
    }

    @Test
    void setUnlimitedFps_setsTargetFps_toIntegerMax() {
        loop.unlockFps();

        assertThat(loop.targetFps()).isEqualTo(Integer.MAX_VALUE);
    }

    private Updatable stopAfterOneFrameUpdatable() {
        return () -> loop.stop();
    }
}
