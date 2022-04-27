package de.suzufa.screwbox.tiled;

public interface Property {

    String name();

    String get();

    int getInt();

    double getDouble();

    boolean hasValue();

    boolean getBoolean();
}
