package dev.screwbox.playground.ai;

import dev.screwbox.core.environment.Component;

public class BoidComponent implements Component {

    public double perceptionRadius = 40;
    public double obstaclePerceptionRadius =120;

    public double velocity = 100;
    public double alignmentStrenth = 5.8;
    public double separationStrength = 8.8;
    public double cohesionStrength = 7.1;
    public double obstacleAvoidanceStrength=10;
}
