package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Attaches the {@link Entity} containing this {@link Component} to the {@link Entity} with the specified {@link Entity#id()}.
 */
public class AttachmentComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public int targetId;
    public Vector offset;

    public AttachmentComponent(final int targetId) {
        this(targetId, Vector.zero());
    }

    public AttachmentComponent(final int targetId, final Vector offset) {
        this.targetId = targetId;
        this.offset = offset;
    }
}
