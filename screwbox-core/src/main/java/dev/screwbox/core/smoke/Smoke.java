package dev.screwbox.core.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;

public interface Smoke {

    Smoke enable();

    Smoke disable();

    Smoke emit(Vector position, double amount, Color color);
    Smoke affect(Vector position, Vector velocity);
}
