package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;

//TODO add to graphics guide
//TODO changelog
//TODO finish up javadoc
//TODO add support for black smoke
/**
 * Add real-time, interactive smoke effects using fluid dynamics.
 *
 * @since 3.33.0
 */
public interface Smoke {

    //TODO implement config.autoTurnOnSmoke

    Smoke emit(Vector position, Vector velocity, double amount, Color color);

    Smoke emit(Vector position, double amount, Color color);

    Smoke push(Vector position, Vector velocity);

    Smoke render(double delta);

    //TODO Smoke absorb(Vector position,  double amount);
    //TODO Smoke setWind(Vector vector);
    Smoke addObstacle(Bounds bounds);

    //TODO double densityAt(Vector)
    //TODO Vector velocityAt(Vector)

    //TODO Smoke setOptions(SmokeOptions.styling(GREYSCALE));
}
