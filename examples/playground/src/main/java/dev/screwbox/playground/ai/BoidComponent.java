package dev.screwbox.playground.ai;

import dev.screwbox.core.environment.Component;

public class BoidComponent implements Component {

    public double visionRadius = 150;

    public double velocity = 100;
    public double steeringStrength = 1.8;
}
