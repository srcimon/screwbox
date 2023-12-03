package io.github.srcimon.screwbox.core.ecosphere.components;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.ecosphere.Component;

import java.io.Serial;

public class GravityComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector gravity;

    public GravityComponent(Vector gravity) {
        this.gravity = gravity;
    }

}
