package io.github.srcimon.screwbox.core.graphics;

/**
 * Adds {@link #width()} and {@link #height()} methods to any class that has a size.
 */
public interface Sizeable {

    /**
     * Get the size.
     */
    Size size();

    /**
     * Get the width.
     */
    default int width() {
        return size().width();
    }

    /**
     * Get the height.
     */
    default int height() {
        return size().height();
    }
}
