package de.suzufa.screwbox.tiled;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.tiled.internal.entity.PropertyEntity;

public class Properties {

    private final List<Property> properties;

    Properties(final List<PropertyEntity> propertyEntities) {
        this.properties = isNull(propertyEntities)
                ? emptyList()
                : propertyEntities.stream()
                        .map(Property::new)
                        .map(Property.class::cast)
                        .toList();
    }

    public Optional<String> get(final String name) {
        Optional<Property> property = findProperty(name);
        return property.isPresent()
                ? Optional.of(property.get().get())
                : Optional.empty();
    }

    private Optional<Property> findProperty(final String name) {
        return properties.stream()
                .filter(p -> name.equals(p.name()))
                .findFirst();
    }

    public Optional<Integer> getInt(String name) {
        Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getInt())
                : Optional.empty();

    }

    public Optional<Double> getDouble(String name) {
        Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getDouble())
                : Optional.empty();
    }

    public String force(String name) {
        return get(name).orElseThrow(() -> missingProperty(name));
    }

    public int forceInt(String name) {
        return getInt(name).orElseThrow(() -> missingProperty(name));
    }

    public double forceDouble(String name) {
        return getDouble(name).orElseThrow(() -> missingProperty(name));
    }

    public List<Property> allEntries() {
        return properties;
    }

    public Optional<Boolean> getBoolean(String name) {
        Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getBoolean())
                : Optional.empty();
    }

    public boolean forceBoolean(String name) {
        return getBoolean(name).orElseThrow(() -> missingProperty(name));
    }

    private IllegalStateException missingProperty(String name) {
        return new IllegalStateException("missing property: " + name);
    }

}
