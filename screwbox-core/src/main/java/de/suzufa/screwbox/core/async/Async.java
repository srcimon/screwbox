package de.suzufa.screwbox.core.async;

import de.suzufa.screwbox.core.entities.Component;

public interface Async {

    boolean hasActiveTasks(Component context);

    Async run(Component context, Runnable task);

}
