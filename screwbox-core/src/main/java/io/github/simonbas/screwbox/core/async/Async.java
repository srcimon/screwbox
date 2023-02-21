package io.github.simonbas.screwbox.core.async;

import io.github.simonbas.screwbox.core.Engine;

/**
 * Execute long running tasks within the {@link Engine}.
 */
public interface Async {

    /**
     * Returns true if there are any active tasks running in the given context.
     */
    boolean hasActiveTasks(Object context);

    /**
     * Runs a task in the given context. The context can be any object that
     * correlates to the task execution.
     */
    Async run(Object context, Runnable task);

}