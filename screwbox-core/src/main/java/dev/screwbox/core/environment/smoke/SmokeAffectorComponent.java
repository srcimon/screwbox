package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

public class SmokeAffectorComponent implements Component {

    public Vector speed;

    public SmokeAffectorComponent() {

    }

    public SmokeAffectorComponent(Vector speed) {
        this.speed = speed;
    }
}
