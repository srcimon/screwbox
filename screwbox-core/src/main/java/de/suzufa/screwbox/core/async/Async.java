package de.suzufa.screwbox.core.async;

public interface Async {

    boolean hasActiveTasks(Object context);

    Async run(Object context, Runnable task);

}