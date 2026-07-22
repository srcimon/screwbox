package dev.screwbox.core.smoke;

import dev.screwbox.core.Vector;

public interface Smoke {

    Smoke enable();

    Smoke disable();

    Smoke emit(Vector position, double amount);
    Smoke affect(Vector position, Vector velocity);
}
