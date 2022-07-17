package de.suzufa.screwbox.tiled.internal;

import de.suzufa.screwbox.tiled.Property;
import de.suzufa.screwbox.tiled.internal.entity.PropertyEntity;

public class DefaultProperty implements Property {

    private final PropertyEntity propertyEntity;

    public DefaultProperty(final PropertyEntity propertyEntity) {
        this.propertyEntity = propertyEntity;
    }

    @Override
    public String name() {
        return propertyEntity.name();
    }

    @Override
    public String get() {
        return propertyEntity.value();
    }

    @Override
    public int getInt() {
        try {
            return Integer.valueOf(get());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("property " + name() + " is not a number: " + get());
        }
    }

    @Override
    public double getDouble() {
        try {
            return Double.valueOf(get());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("property " + name() + " is not a number: " + get());
        }
    }

    @Override
    public boolean hasValue() {
        return !get().isEmpty();
    }

    @Override
    public boolean getBoolean() {
        return "true".equalsIgnoreCase(get());
    }

}
