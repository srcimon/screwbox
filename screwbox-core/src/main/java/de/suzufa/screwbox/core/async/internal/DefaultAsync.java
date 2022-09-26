package de.suzufa.screwbox.core.async.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.async.Async;

public class DefaultAsync implements Async {

    private final ExecutorService executor;
    private final Map<UUID, Object> runningTasks = new HashMap<>();

    public DefaultAsync(final ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public boolean contextHasActiveTasks(final Object context) {
        if (runningTasks.values().contains(context)) {
            System.out.println("!!!");
        }
        return runningTasks.values().contains(context);
    }

    @Override
    public void runInContext(final Object context, final Runnable task) {
        final UUID id = UUID.randomUUID();
        runningTasks.put(id, context);
        executor.submit(new Runnable() {

            @Override
            public void run() {
                task.run();
                runningTasks.remove(id);
            }
        });
    }

}
