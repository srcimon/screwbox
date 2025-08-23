package dev.screwbox.core.loop.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.test.TestUtil.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

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
        assertThat(loop.runningTime()).isEqualTo(Duration.none());
    }

    @Test
    void runningTime_started_returnsDuration() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        assertThat(loop.runningTime()).isNotEqualTo(Duration.none());
    }

    @Test
    void runningTime_startedAgain_returnsDurationSinceSecondStart() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        Time time = Time.now();

        loop.start();

        assertThat(loop.runningTime()).isNotEqualTo(Duration.none());
        assertThat(loop.runningTime().isLessThan(Duration.since(time))).isTrue();
    }

    @Test
    void startTime_notStarted_returnsUnset() {
        assertThat(loop.startTime()).isEqualTo(Time.unset());
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
    void setTargetFps_targetFpsBelowMinFps_throwsException() {
        assertThatThrownBy(() -> loop.setTargetFps(80))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("target fps must be at least 120");
    }

    @Test
    void awaitTermination_threadWasStoppedBecauseOfException_doesntBlockEndlessly() {
        updatables.add(() -> {
            throw new IllegalStateException("exception in loop");

        });

        assertThatThrownBy(loop::start)
                .isInstanceOf(IllegalStateException.class);

        assertThatNoException().isThrownBy(loop::awaitTermination);
    }

    @Test
    void awaitTermination_threadWasStoppedBecauseOfSystem_doesntBlockEndlessly() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.start();

        assertThatNoException().isThrownBy(loop::awaitTermination);
    }

    @Test
    void unlockFps_setsTargetFps_toIntegerMax() {
        loop.unlockFps();

        assertThat(loop.targetFps()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void updateDuration_oneSystemNeedsAtLeast10ms_isBetween10and100ms() {
        updatables.add(stopAfterOneFrameUpdatable());
        updatables.add(() -> sleep(ofMillis(10)));
        loop.start();

        var updateDuration = loop.updateDuration();

        assertThat(updateDuration.milliseconds()).isBetween(10L, 100L);
    }

    @Test
    void updateDuration_oneSystemNeedsAtLeast100ms_isGreaterThan100ms() {
        updatables.add(stopAfterOneFrameUpdatable());
        updatables.add(() -> sleep(ofMillis(100)));
        loop.start();

        var updateDuration = loop.updateDuration();

        assertThat(updateDuration.milliseconds()).isGreaterThanOrEqualTo(100);
    }

    @Test
    void time_onlyOneSystem_isAfterStartingLoop() {
        updatables.add(stopAfterOneFrameUpdatable());

        Time before = Time.now();
        loop.start();

        assertThat(loop.time().isAfter(before)).isTrue();
    }

    @Test
    void setSpeed_validSpeed_setsSpeed() {
        updatables.add(stopAfterOneFrameUpdatable());

        loop.setSpeed(2.0);

        loop.start();

        assertThat(loop.speed()).isEqualTo(2.0);
        assertThat(loop.delta()).isPositive();
    }

    @Test
    void setSpeed_negativeSpeed_throwsException() {
        assertThatThrownBy(() -> loop.setSpeed(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("speed must be positive (actual value: -0.1)");
    }

    @Test
    void setSpeed_speedTooHeight_throwsException() {
        assertThatThrownBy(() -> loop.setSpeed(11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("speed cannot exceed 10.0 (actual value: 11.0)");
    }

    private Updatable stopAfterOneFrameUpdatable() {
        return () -> loop.stop();
    }
}
