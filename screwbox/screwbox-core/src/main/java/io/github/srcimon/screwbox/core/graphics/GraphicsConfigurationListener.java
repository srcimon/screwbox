package io.github.srcimon.screwbox.core.graphics;

import java.util.EventListener;

/**
 * Listener interface for receiving {@link GraphicsConfigurationEvent}s.
 */
public interface GraphicsConfigurationListener extends EventListener {

    /**
     * invoked when a property of the {@link GraphicsConfiguration} is changed.
     */
    void configurationChanged(GraphicsConfigurationEvent event);
}