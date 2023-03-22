package io.github.simonbas.screwbox.core.entities;

import java.util.EventListener;

//TODO add EntityEvent
public interface EntityListener extends EventListener {

    void componentAdded(EntityEvent event);

    void componentRemoved(EntityEvent event);
}
