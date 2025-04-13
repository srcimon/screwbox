package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Moves the {@link Entity} towards the current mouse cursor position.
 */
public class CursorAttachmentComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Specify entity offset relative to mouse cursor.
     */
    public final Vector offset;

    /**
     * Create a new instance without offset.
     */
    public CursorAttachmentComponent() {
        this(Vector.zero());
    }

    /**
     * Create a new instance with specified offset.
     */
    public CursorAttachmentComponent(final Vector offset) {
        this.offset = offset;
    }
}
