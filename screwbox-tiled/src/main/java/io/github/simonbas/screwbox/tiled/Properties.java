package io.github.simonbas.screwbox.tiled;

import io.github.simonbas.screwbox.tiled.internal.PropertyEntity;

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
                        .map(Property.class::cast)
                        .toList();
    }

    public Optional<String> get(final String name) {
        final Optional<Property> property = findProperty(name);
        return property.isPresent()
                ? Optional.of(property.get().get())
                : Optional.empty();
    }

    private Optional<Property> findProperty(final String name) {
        return propertyList.stream()
                .filter(p -> name.equals(p.name()))
                .findFirst();
    }

    public Optional<Integer> getInt(final String name) {
        final Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getInt())
                : Optional.empty();

    }

    public Optional<Double> getDouble(final String name) {
        final Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getDouble())
                : Optional.empty();
    }

    public String force(final String name) {
        return get(name).orElseThrow(() -> missingProperty(name));
    }

    public int forceInt(final String name) {
        return getInt(name).orElseThrow(() -> missingProperty(name));
    }

    public double forceDouble(final String name) {
        return getDouble(name).orElseThrow(() -> missingProperty(name));
    }

    public List<Property> all() {
        return propertyList;
    }

    public Optional<Boolean> getBoolean(final String name) {
        final Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getBoolean())
                : Optional.empty();
    }

    public boolean forceBoolean(final String name) {
        return getBoolean(name).orElseThrow(() -> missingProperty(name));
    }

    private IllegalStateException missingProperty(final String name) {
        return new IllegalStateException("missing property: " + name);
    }

}
