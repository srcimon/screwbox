package de.suzufa.screwbox.core.entities;

import java.util.EventListener;

public interface EntityListener extends EventListener {

    void componentAdded(Entity entity);

    void componentRemoved(Entity entity);
}
