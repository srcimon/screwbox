package io.github.srcimon.screwbox.core.graphics;

/**
 * Rendering always happens within the {@link Viewport}. Without any other specification the {@link Viewport} is
 * identical with the {@link Screen}.
 */
//TODO: document how to change the viewport
public interface Viewport extends Sizeable {

    Offset offset();

    Offset center();
}
