package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;
//TODO document test
public class SmokePusherComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector speed;

    public SmokePusherComponent() {

    }

    public SmokePusherComponent(Vector speed) {
        this.speed = speed;
    }
}
