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
    public double restLength;
    public double retractStrength = 20;
    public double expandStrength = 20;

}
