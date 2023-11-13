package io.github.srcimon.screwbox.core.window;

import java.util.EventListener;

/**
 * Listener interface for receiving {@link WindowDropEvent}s.
 */
//TODO This is kind of a mess - eventing may result in concurrent modification exception - better change to pull method from window - this is not very resource effecient but it's better than a bug
public interface WindowDropListener extends EventListener {

    /**
     * Invoced when one ore multiple files are dropped on the {@link Window}.
     * E.g. dragging a text file from users desktop over the application {@link Window}.
     */
    void filesDroppedOnWindow(WindowDropEvent event);
}
