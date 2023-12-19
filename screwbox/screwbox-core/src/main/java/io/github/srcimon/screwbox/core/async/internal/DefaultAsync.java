package io.github.srcimon.screwbox.core.async.internal;

import io.github.srcimon.screwbox.core.async.Async;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static java.util.Objects.requireNonNull;

public class DefaultAsync implements Async {

    private final ExecutorService executor;
    private final Map<UUID, Object> runningTasks = new ConcurrentHashMap<>();

    public DefaultAsync(final ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public boolean hasActiveTasks(final Object context) {
        requireNonNull(context, "context must not be null");
        return runningTasks.containsValue(context);
    }

    @Override
    public Async run(final Object context, final Runnable task) {
        requireNonNull(context, "context must not be null");
        requireNonNull(task, "task must not be null");

        final UUID id = UUID.randomUUID();
        runningTasks.put(id, context);
        executor.execute(() -> {
            try {
                task.run();
            } catch (final Exception exception) {
                throw new IllegalStateException("Exception in asynchronous context: " + context, exception);
            }
            runningTasks.remove(id);
        });
        return this;
    }

    @Override
    public Async runExclusive(final Object context, final Runnable task) {
        if (!hasActiveTasks(context)) {
            run(context, task);
        }
        return this;
    }

}
