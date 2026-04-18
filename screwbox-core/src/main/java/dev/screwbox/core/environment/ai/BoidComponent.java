package dev.screwbox.core.environment.ai;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

//TODO document properties

/**
 * Adds boid (bird-oid object) like behaviour to a physics entity.
 *
 * @since 3.27.0
 */
public class BoidComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double perceptionRadius = 40;
    public double obstaclePerceptionRadius = 120;
    public boolean perceptFrontalOnly = true;
    public double velocity = 100;
    public double alignmentStrenth = 6.8;
    public double separationStrength = 10.8;
    public double cohesionStrength = 4.1;
    public double obstacleAvoidanceStrength = 10;
}
