package dev.screwbox.tiled;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.tiled.internal.PropertyEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

public class Properties {

    private final List<Property> propertyList;

    Properties(final List<PropertyEntity> propertyEntities) {
        this.propertyList = isNull(propertyEntities)
            ? emptyList()
            : propertyEntities.stream()
            .map(Property::new)
            .toList();
    }

    /**
     * Try to get an enum value from property with specified name.
     *
     * @since 2.17.0
     */
    public <T extends Enum<?>> Optional<T> tryGetEnum(final String name, final Class<T> enumClazz) {
        return tryGetString(name).flatMap(value -> Arrays.stream(enumClazz.getEnumConstants())
            .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(value))
            .findFirst());
    }

    /**
     * Try to get a string value with specified name.
     */
    public Optional<String> tryGetString(final String name) {
        return tryGet(name).map(Property::get);
    }

    /**
     * Try to get an integer value with specified name.
     */
    public Optional<Integer> tryGetInt(final String name) {
        final Optional<Property> property = tryGet(name);
        return property.isPresent() && property.get().hasValue()
            ? Optional.of(property.get().getInt())
            : Optional.empty();

    }

    /**
     * Try to get a double value with specified name.
     */
    public Optional<Double> tryGetDouble(final String name) {
        final Optional<Property> property = tryGet(name);
        return property.isPresent() && property.get().hasValue()
            ? Optional.of(property.get().getDouble())
            : Optional.empty();
    }

    /**
     * Get a string value with specified name.
     */
    public String getString(final String name) {
        return tryGetString(name).orElseThrow(() -> missingProperty(name));
    }

    /**
     * Get an integer value with specified name.
     */
    public int getInt(final String name) {
        return tryGetInt(name).orElseThrow(() -> missingProperty(name));
    }

    /**
     * Get a double value with specified name.
     */
    public double getDouble(final String name) {
        return tryGetDouble(name).orElseThrow(() -> missingProperty(name));
    }

    /**
     * Returns all properties.
     */
    public List<Property> all() {
        return propertyList;
    }

    /**
     * Try to get a booleanvalue with specified name.
     */
    public Optional<Boolean> tryGetBoolean(final String name) {
        final Optional<Property> property = tryGet(name);
        return property.isPresent() && property.get().hasValue()
            ? Optional.of(property.get().getBoolean())
            : Optional.empty();
    }

    /**
     * Get a boolean value with specified name.
     */
    public boolean getBoolean(final String name) {
        return tryGetBoolean(name).orElseThrow(() -> missingProperty(name));
    }

    /**
     * Try to get a {@link Color} value with specified name.
     *
     * @since 3.32.0
     */
    public Optional<Color> tryGetColor(final String name) {
        final Optional<Property> property = tryGet(name);
        return property.isPresent() && property.get().hasValue()
            ? Optional.of(property.get().getColor())
            : Optional.empty();
    }

    /**
     * Get a {@link Color} value with specified name.
     *
     * @since 3.32.0
     */
    public Color getColor(final String name) {
        return tryGetColor(name).orElseThrow(() -> missingProperty(name));
    }

    private Optional<Property> tryGet(final String name) {
        return propertyList.stream()
            .filter(property -> name.equals(property.name()))
            .findFirst();
    }

    private IllegalStateException missingProperty(final String name) {
        return new IllegalStateException("missing property: " + name);
    }
}
