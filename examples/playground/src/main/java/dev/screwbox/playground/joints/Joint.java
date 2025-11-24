package dev.screwbox.playground.joints;

import dev.screwbox.core.Angle;

import java.io.Serial;
import java.io.Serializable;

public class Joint implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int targetEntityId;

    public Joint(int targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public int targetEntityId() {
        return targetEntityId;
    }

    public Angle angle = Angle.none();
    public double length;
    public double retract = 20;
    public double expand = 20;
    public double stiffness = 20;

}
