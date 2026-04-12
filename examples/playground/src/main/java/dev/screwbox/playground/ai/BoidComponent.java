package dev.screwbox.playground.ai;

import dev.screwbox.core.environment.Component;

public class BoidComponent implements Component {

    public double visionRadius = 40;
    public double obstacleVisionRadius=120;

    public double velocity = 100;
    public double alignmentStrenth = 5.8;
    public double separationStrength = 8.8;
    public double cohesionStrength = 6.1;
    public double obstacleAvoidanceStrength=10;
}
