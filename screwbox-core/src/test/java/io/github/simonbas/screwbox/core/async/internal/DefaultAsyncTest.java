package io.github.simonbas.screwbox.core.async.internal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultAsyncTest {

    @Mock
    Consumer<Throwable> exceptionHandler;

    DefaultAsync async;

    ExecutorService executor;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newSingleThreadExecutor();
        async = new DefaultAsync(executor, exceptionHandler);
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
        assertThatThrownBy(() -> async.run(null, () -> someLongRunningTask()))
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
    void run_taskThrowsException_exeptionHandlerIsInformed() {
        async.run("myContext", () -> {
            throw new IllegalArgumentException("Other thread Exception");
        });

        while (async.hasActiveTasks("myContext")) {
            // wait
        }

        assertThat(async.hasActiveTasks("myContext")).isFalse();
        var exceptionCaptor = ArgumentCaptor.forClass(RuntimeException.class);
        verify(exceptionHandler).accept(exceptionCaptor.capture());

        assertThat(exceptionCaptor.getValue())
                .hasMessage("Exception in asynchronous context: myContext")
                .hasCauseInstanceOf(IllegalArgumentException.class);
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
