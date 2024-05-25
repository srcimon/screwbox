package io.github.srcimon.screwbox.vacuum;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;
//TODO Make engine default
public class AttachmentComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public int targetId;
    public Vector displacement;

    public AttachmentComponent(final int targetId) {
        this(targetId, Vector.zero());
    }

    public AttachmentComponent(final  int targetId, final  Vector displacement) {
        this.targetId = targetId;
        this.displacement = displacement;
    }
}
