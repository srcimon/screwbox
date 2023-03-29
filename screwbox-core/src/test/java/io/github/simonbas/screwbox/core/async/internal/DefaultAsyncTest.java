package io.github.simonbas.screwbox.core.async.internal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultAsyncTest {

    DefaultAsync async;

    ExecutorService executor;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        async = new DefaultAsync(executor);
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
        async.run("myContext", () -> someLongRunningTask());
        assertThat(async.hasActiveTasks("myContext")).isTrue();
    }

    @Test
    void hasActiveTasks_hasTasksInAnotherContext_isFalse() {
        async.run("another Context", () -> someLongRunningTask());
        assertThat(async.hasActiveTasks("myContext")).isFalse();
    }

    @Test
    void hasActiveTasks_taskCompleted_isFalse() {
        async.run("myContext", () -> someLongRunningTask());

        while (async.hasActiveTasks("myContext")) {
            // wait
        }

        assertThat(async.hasActiveTasks("myContext")).isFalse();
    }

    @Test
    void runSingle_taskAlreadyRunning_noSecondExecution() {
        async.run("myContext", () -> someLongRunningTask());

        assertThatNoException().isThrownBy(() -> {
            async.runSingle("myContext", () -> {
                throw new IllegalStateException("I would crash if i would start");
            });
        });
    }

    @Test
    void runSingle_isOnlyTaskInContext_startsTask() {
        async.runSingle("myContext", () -> someLongRunningTask());

        boolean hasActiveTasks = async.hasActiveTasks("myContext");
        assertThat(hasActiveTasks).isTrue();
    }

    private void someLongRunningTask() {
        // do nothing
    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
