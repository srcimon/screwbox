package io.github.simonbas.screwbox.core.window;

import java.util.EventListener;

/**
 * Listener interface for receiving {@link WindowDropEvent}s.
 */
public interface WindowDropListener extends EventListener {

    /**
     * Invoced when one ore multiple files are dropped on the {@link Window}.
     * E.g. dragging a text file from users desktop over the application {@link Window}.
     */
    void filesDroppedOnWindow(WindowDropEvent event);
}
