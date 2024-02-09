package io.github.srcimon.screwbox.core.audio;

import java.util.EventListener;

/**
 * Listener interface for receiving {@link AudioConfigurationEvent}s.
 */
@FunctionalInterface
public interface AudioConfigurationListener extends EventListener {

    /**
     * invoked when a property of the {@link AudioConfiguration} is changed.
     */
    void configurationChanged(AudioConfigurationEvent event);
}