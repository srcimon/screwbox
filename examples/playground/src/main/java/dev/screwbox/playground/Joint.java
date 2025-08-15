package dev.screwbox.playground;

public class Joint {

    public Joint(int targetEntityId) {
        this.targetEntityId = targetEntityId;
    }
    public int targetEntityId;
    public double length;
    public double retractStrength = 40;
    public double expandStrength = 40;
}
