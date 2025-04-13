package dev.screwbox.tiles;

import dev.screwbox.tiles.internal.PropertyEntity;

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
     * Try to get enum value from property with specified name.
     *
     * @since 2.17.0
     */
    public <T extends Enum<?>> Optional<T> tryGetEnum(final String name, final Class<T> enumClazz) {
        return tryGetString(name).flatMap(value -> Arrays.stream(enumClazz.getEnumConstants())
                .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(value))
                .findFirst());
    }

    public Optional<String> tryGetString(final String name) {
        return tryGet(name).map(Property::get);
    }

    private Optional<Property> tryGet(final String name) {
        return propertyList.stream()
                .filter(p -> name.equals(p.name()))
                .findFirst();
    }

    public Optional<Integer> tryGetInt(final String name) {
        final Optional<Property> property = tryGet(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getInt())
                : Optional.empty();

    }

    public Optional<Double> tryGetDouble(final String name) {
        final Optional<Property> property = tryGet(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getDouble())
                : Optional.empty();
    }

    public String getString(final String name) {
        return tryGetString(name).orElseThrow(() -> missingProperty(name));
    }

    public int getInt(final String name) {
        return tryGetInt(name).orElseThrow(() -> missingProperty(name));
    }

    public double getDouble(final String name) {
        return tryGetDouble(name).orElseThrow(() -> missingProperty(name));
    }

    public List<Property> all() {
        return propertyList;
    }

    public Optional<Boolean> tryGetBoolean(final String name) {
        final Optional<Property> property = tryGet(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getBoolean())
                : Optional.empty();
    }

    public boolean getBoolean(final String name) {
        return tryGetBoolean(name).orElseThrow(() -> missingProperty(name));
    }

    private IllegalStateException missingProperty(final String name) {
        return new IllegalStateException("missing property: " + name);
    }

}
