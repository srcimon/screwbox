package de.suzufa.screwbox.core.async.internal;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import de.suzufa.screwbox.core.async.Async;

public class DefaultAsync implements Async {

    private final ExecutorService executor;
    private final Map<UUID, Object> runningTasks = new ConcurrentHashMap<>();
    private final Consumer<Throwable> exceptionHandler;

    public DefaultAsync(final ExecutorService executor, final Consumer<Throwable> exceptionHandler) {
        this.executor = executor;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public boolean hasActiveTasks(final Object context) {
        requireNonNull(context, "context must not be null");
        return runningTasks.values().contains(context);
    }

    @Override
    public Async run(final Object context, final Runnable task) {
        requireNonNull(context, "context must not be null");
        requireNonNull(task, "task must not be null");

        final UUID id = UUID.randomUUID();
        runningTasks.put(id, context);
        executor.submit(() -> {
            try {
                task.run();
            } catch (final Exception exception) {
                final var wrappedException = new RuntimeException("Exception in asynchronous context: " + context,
                        exception);
                exceptionHandler.accept(wrappedException);
            }
            runningTasks.remove(id);
        });
        return this;
    }

}
