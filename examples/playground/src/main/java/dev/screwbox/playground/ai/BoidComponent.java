package dev.screwbox.playground.ai;

import dev.screwbox.core.environment.Component;

public class BoidComponent implements Component {

    public double visionRadius = 60;
    public double obstacleVisionRadius=200;

    public double velocity = 100;
    public double alignmentStrenth = 4.8;
    public double separationStrength = 6.8;
    public double cohesionStrength = 5.4;
}
