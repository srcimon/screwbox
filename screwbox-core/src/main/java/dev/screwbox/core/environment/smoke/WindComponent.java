package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class WindComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector velocity;

    public WindComponent(final Vector velocity) {
        this.velocity = velocity;
    }
}
