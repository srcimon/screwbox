package dev.screwbox.playground;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;

public class JointComponent implements Component {

    public int targetEntityId;
    public double length;
    public double strength = 20;

    public JointComponent(int targetEntityId) {
        this.targetEntityId = targetEntityId;
    }
}
