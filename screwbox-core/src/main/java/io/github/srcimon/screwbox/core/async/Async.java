package io.github.srcimon.screwbox.core.async;

import io.github.srcimon.screwbox.core.Engine;

/**
 * Execute long running tasks within the {@link Engine}.
 *
 * @see <a href="http://screwbox.dev/docs/core-modules/async">Documentation</a>
 */
public interface Async {

    /**
     * Returns true if there are any active tasks running in the given context.
     */
    boolean hasActiveTasks(Object context);

    /**
     * Runs a task in the given context. The context can be any object that
     * correlates to the task execution.
     *
     * @see #runExclusive(Object, Runnable)
     */
    Async run(Object context, Runnable task);

    /**
     * Runs a task in the given context, but only if there is no other task in the context running right now.
     * The context can be any object that correlates to the task execution.
     *
     * @see #run(Object, Runnable)
     */
    Async runExclusive(Object context, Runnable task);

    /**
     * Returns the current count of active tasks.
     */
    int taskCount();
}