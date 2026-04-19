package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Adds boid (bird-oid object) like behaviour to a physics entity.
 *
 * @since 3.27.0
 */
public class BoidComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Other boids can be percepted within this radius.
     */
    public double perceptionRadius = 40;

    /**
     * Configure perception all around vs forward only.
     */
    public boolean perceptFrontalOnly = true;

    /**
     * Configure perception radius for obstacles.
     */
    public double obstaclePerceptionRadius = 120;

    /**
     * Strength of the steer to avoid obstacles.
     */
    public double obstacleAvoidanceStrength = 10;

    /**
     * Velocity of the boid. Boids will not slow down.
     */
    public double velocity = 100;

    /**
     * Strength of steer towards the average heading of local flockmates.
     */
    public double alignmentStrenth = 6.8;

    /**
     * Strength of the steer to avoid crowding local flockmates.
     */
    public double separationStrength = 10.8;

    /**
     * Strength of the steer to move towards the average position (center of mass) of local flockmates.
     */
    public double cohesionStrength = 4.1;

    /**
     * Angle of sensor to detect obstalces. Every boid creates three sensor lines in front based on that angle
     * (-angle, zero, angle).
     */
    public Angle obstacleSensorAngle = Angle.degrees(22.5);
}
