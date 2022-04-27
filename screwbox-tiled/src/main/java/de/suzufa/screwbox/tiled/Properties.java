package de.suzufa.screwbox.tiled;

import java.util.List;
import java.util.Optional;

public interface Properties {

    Optional<String> get(String name);

    Optional<Integer> getInt(String name);

    Optional<Double> getDouble(String name);

    String force(String name);

    int forceInt(String name);

    double forceDouble(String name);

    List<Property> allEntries();

    Optional<Boolean> getBoolean(String name);

    boolean forceBoolean(String name);

}
