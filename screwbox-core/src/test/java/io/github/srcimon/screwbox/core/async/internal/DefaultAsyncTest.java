package io.github.srcimon.screwbox.core.async.internal;

import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@Timeout(1)
@MockitoSettings
class DefaultAsyncTest {

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
        async.run("myContext", () -> someLongRunningTask());
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
        assertThatThrownBy(() -> async.run(null, this::someLongRunningTask))
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
        async.run("myContext", this::someLongRunningTask);
        assertThat(async.hasActiveTasks("myContext")).isTrue();
    }

    @Test
    void hasActiveTasks_hasTasksInAnotherContext_isFalse() {
        async.run("another Context", this::someLongRunningTask);
        assertThat(async.hasActiveTasks("myContext")).isFalse();
    }

    @Test
    void hasActiveTasks_taskCompleted_isFalse() {
        async.run("myContext", this::someLongRunningTask);

        while (async.hasActiveTasks("myContext")) {
            // wait
        }

        assertThat(async.hasActiveTasks("myContext")).isFalse();
    }

    @Test
    void runExclusive_taskAlreadyRunning_noSecondExecution() {
        async.run("myContext", this::someLongRunningTask);

        assertThatNoException().isThrownBy(() -> async.runExclusive("myContext", () -> {
            throw new IllegalStateException("I would crash if i would start");
        }));
    }

    @Test
    void runExclusive_isOnlyTaskInContext_startsTask() {
        async.runExclusive("myContext", this::someLongRunningTask);

        boolean hasActiveTasks = async.hasActiveTasks("myContext");
        assertThat(hasActiveTasks).isTrue();
    }

    private void someLongRunningTask() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    void afterEach() {
        TestUtil.shutdown(executor);
    }

}
