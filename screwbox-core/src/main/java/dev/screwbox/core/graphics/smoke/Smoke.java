package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;

//TODO add to graphics guide
//TODO changelog
//TODO finish up javadoc

/**
 * Subsystem for creating {@link Smoke} effects.
 *
 * @since 3.33.0
 */
public interface Smoke {

    @Deprecated
    Smoke enable();

    @Deprecated
    Smoke disable();

    //TODO implement config.autoTurnOnSmoke

    Smoke emit(Vector position, double amount, Color color);

    Smoke push(Vector position, Vector velocity);

    Smoke render();

    Smoke addObstacle(Bounds bounds);

    //TODO Smoke setOptions(SmokeOptions.styling(GREYSCALE));
}
