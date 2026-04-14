package dev.screwbox.playground.ai;

import dev.screwbox.core.environment.Component;

public class BoidComponent implements Component {

    public double perceptionRadius = 40;
    public double obstaclePerceptionRadius = 120;
    public boolean perceptFrontalOnly = true;
    public double velocity = 100;
    public double alignmentStrenth = 6.8;
    public double separationStrength = 10.8;
    public double cohesionStrength = 4.1;
    public double obstacleAvoidanceStrength = 10;
}
