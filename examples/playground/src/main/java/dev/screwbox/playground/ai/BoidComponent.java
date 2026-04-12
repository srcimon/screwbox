package dev.screwbox.playground.ai;

import dev.screwbox.core.environment.Component;

public class BoidComponent implements Component {

    public double visionRadius = 150;

    public double velocity = 150;
    public double alignmentStrenth = 1.8;
    public double separationStrength = 2.8;
    public double cohesionStrength = 5.4;
}
