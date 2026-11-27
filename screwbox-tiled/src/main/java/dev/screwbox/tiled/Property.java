package dev.screwbox.tiled;

import dev.screwbox.core.graphics.Color;

public class Property {

    private final String name;
    private final String value;

    Property(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String get() {
        return value;
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
