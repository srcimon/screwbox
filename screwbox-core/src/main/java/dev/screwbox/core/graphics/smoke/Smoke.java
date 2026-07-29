package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;

//TODO add to graphics guide
//TODO changelog
//TODO finish up javadoc

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

    Smoke addObstacle(Bounds bounds);

    //TODO Smoke setOptions(SmokeOptions.styling(GREYSCALE));
}
