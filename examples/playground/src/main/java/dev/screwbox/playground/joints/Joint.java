package dev.screwbox.playground.joints;

import dev.screwbox.core.Angle;

public class Joint {

    public Joint(int targetEntityId) {
        this.targetEntityId = targetEntityId;
    }
    public int targetEntityId;
    public double restLength;
    public double retractStrength = 20;
    public double expandStrength = 20;
    public Angle angle = Angle.none();
}
