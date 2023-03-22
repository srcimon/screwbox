package io.github.simonbas.screwbox.core.entities;

import java.util.EventListener;

/**
 * Listener interface for receiving {@link EntityEvent}s.
 */
public interface EntityListener extends EventListener {

    /**
     * invoked when a {@link Component} was added to the {@link Entity}.
     */
    void componentAdded(EntityEvent event);

    /**
     * invoked when a {@link Component} was removed from the {@link Entity}.
     */
    void componentRemoved(EntityEvent event);
}
