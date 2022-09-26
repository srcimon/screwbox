package de.suzufa.screwbox.core.async.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertThatThrownBy(() -> async.run(null, () -> sleepForMs(100)))
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
        async.run("myContext", () -> sleepForMs(100));
        assertThat(async.hasActiveTasks("myContext")).isTrue();
    }

    @Test
    void hasActiveTasks_hasTasksInAnotherContext_isFalse() {
        async.run("another Context", () -> sleepForMs(100));
        assertThat(async.hasActiveTasks("myContext")).isFalse();
    }

    @Test
    void hasActiveTasks_taskCompleted_isFalse() {
        async.run("myContext", () -> sleepForMs(400));

        while (async.hasActiveTasks("myContext")) {
            sleepForMs(100);
        }

        assertThat(async.hasActiveTasks("myContext")).isFalse();
    }

    @AfterEach
    void afterEach() {
        executor.shutdown();
    }

    private void sleepForMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
