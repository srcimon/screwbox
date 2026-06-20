package dev.screwbox.tiled;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.tiled.internal.PropertyEntity;

public class Property {

    private final PropertyEntity propertyEntity;

    Property(final PropertyEntity propertyEntity) {
        this.propertyEntity = propertyEntity;
    }

    public String name() {
        return propertyEntity.name();
    }

    public String get() {
        return propertyEntity.value();
    }

    public int getInt() {
        try {
            return Integer.parseInt(get());
        } catch (final NumberFormatException e) {
            throw new IllegalStateException("property " + name() + " is not a number: " + get());
        }
    }

    public double getDouble() {
        try {
            return Double.parseDouble(get());
        } catch (final NumberFormatException e) {
            throw new IllegalStateException("property " + name() + " is not a number: " + get());
        }
    }

    public boolean hasValue() {
        return !get().isEmpty();
    }

    public boolean getBoolean() {
        return "true".equalsIgnoreCase(get());
    }

    /**
     * Returns the property value as {@link Color}.
     *
     * @since 3.32.0
     */
    public Color getColor() {
        return Color.hex(get());
    }
}
