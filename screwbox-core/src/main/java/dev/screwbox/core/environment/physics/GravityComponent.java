package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class GravityComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector gravity;

    public GravityComponent(Vector gravity) {
        this.gravity = gravity;
    }

}
