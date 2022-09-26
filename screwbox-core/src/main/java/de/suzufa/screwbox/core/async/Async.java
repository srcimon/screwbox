package de.suzufa.screwbox.core.async;

public interface Async {

    boolean contextHasActiveTasks(Object context);

    default boolean hasNoActiveTasksInContext(Object context) {
        return !contextHasActiveTasks(context);
    }

    void runInContext(Object context, Runnable task);

}
