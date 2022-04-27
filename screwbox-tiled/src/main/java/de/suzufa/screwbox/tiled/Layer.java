package de.suzufa.screwbox.tiled;

import java.util.Optional;

import de.suzufa.screwbox.core.Percentage;

public interface Layer {

    String name();

    int order();

    Properties properties();

    Percentage opacity();

    boolean isImageLayer();

    Optional<String> image();

    double parallaxX();

    double parallaxY();
}
