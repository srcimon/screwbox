package de.suzufa.screwbox.core.async.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.async.Async;
import de.suzufa.screwbox.core.entities.Component;

public class DefaultAsync implements Async {

    private final ExecutorService executor;
    private final Map<UUID, Component> runningTasks = new HashMap<>();

    public DefaultAsync(final ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public boolean hasActiveTasks(final Component context) {
        return runningTasks.values().contains(context);
    }

    @Override
    public Async run(final Component context, final Runnable task) {
        final UUID id = UUID.randomUUID();
        runningTasks.put(id, context);
        executor.submit(new Runnable() {

            @Override
            public void run() {
                task.run();
                runningTasks.remove(id);
            }
        });
        return this;
    }

}
