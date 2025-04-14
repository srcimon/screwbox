package dev.screwbox.core.async.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Timeout(1)
@MockitoSettings
class DefaultAsyncTest {

    private static final Duration SLEEP_DURATION = Duration.ofMillis(100);

    DefaultAsync async;

    ExecutorService executor;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        async = new DefaultAsync(executor);
    }

    @Test
    void taskCount_noTasks_isZero() {
        assertThat(async.taskCount()).isZero();
    }

    @Test
    void taskCount_oneTask_isOne() {
        async.run("myContext", () -> TestUtil.sleep(SLEEP_DURATION));
        assertThat(async.taskCount()).isOne();
    }

    @Test
    void hasActiveTasks_noTasks_isFalse() {
        boolean hasActiveTasks = async.hasActiveTasks("new-context");
        assertThat(hasActiveTasks).isFalse();
    }

    @Test
    void hasActiveTasks_contextNull_exception() {
        assertThatThrownBy(() -> async.hasActiveTasks(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("context must not be null");
    }

    @Test
    void run_contextNull_exception() {
        assertThatThrownBy(() -> async.run(null, () -> TestUtil.sleep(SLEEP_DURATION)))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("context must not be null");
    }

    @Test
    void run_taskIsNull_exception() {
        assertThatThrownBy(() -> async.run("context", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("task must not be null");
    }

    @Test
    void hasActiveTasks_hasTasksInSameContext_isTrue() {
        async.run("myContext", () -> TestUtil.sleep(SLEEP_DURATION));
        assertThat(async.hasActiveTasks("myContext")).isTrue();
    }

    @Test
    void hasActiveTasks_hasTasksInAnotherContext_isFalse() {
        async.run("another Context", () -> TestUtil.sleep(SLEEP_DURATION));
        assertThat(async.hasActiveTasks("myContext")).isFalse();
    }

    @Test
    void hasActiveTasks_taskCompleted_isFalse() {
        async.run("myContext", () -> TestUtil.sleep(SLEEP_DURATION));

        TestUtil.await(() -> !async.hasActiveTasks("myContext"), Duration.oneSecond());

        assertThat(async.hasActiveTasks("myContext")).isFalse();
    }

    @Test
    void runExclusive_taskAlreadyRunning_noSecondExecution() {
        async.run("myContext", () -> TestUtil.sleep(SLEEP_DURATION));

        assertThatNoException().isThrownBy(() -> async.runExclusive("myContext", () -> {
            throw new IllegalStateException("I would crash if i would start");
        }));
    }

    @Test
    void runExclusive_isOnlyTaskInContext_startsTask() {
        async.runExclusive("myContext", () -> TestUtil.sleep(SLEEP_DURATION));

        boolean hasActiveTasks = async.hasActiveTasks("myContext");
        assertThat(hasActiveTasks).isTrue();
    }

    @AfterEach
    void afterEach() {
        TestUtil.shutdown(executor);
    }

}
