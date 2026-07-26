package dev.screwbox.core.smoke;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;

public interface Smoke {//TODO Vapor?

    Smoke enable();

    Smoke disable();

    Smoke emit(Vector position, double amount, Color color);
    Smoke affect(Vector position, Vector velocity);

    void render();

    Smoke addObstacle(Bounds bounds);

    //TODO Smoke setOptions(SmokeOptions.styling(GREYSCALE));
}
