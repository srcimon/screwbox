package de.suzufa.screwbox.tiled.internal;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.Property;
import de.suzufa.screwbox.tiled.internal.entity.PropertyEntity;

public class DefaultProperties implements Properties {

    private final List<Property> properties;

    public static DefaultProperties empty() {
        return new DefaultProperties(Collections.emptyList());
    }

    public DefaultProperties(final List<PropertyEntity> propertyEntities) {
        this.properties = isNull(propertyEntities)
                ? emptyList()
                : propertyEntities.stream()
                        .map(DefaultProperty::new)
                        .map(Property.class::cast)
                        .toList();
    }

    @Override
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

    @Override
    public Optional<Integer> getInt(String name) {
        Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getInt())
                : Optional.empty();

    }

    @Override
    public Optional<Double> getDouble(String name) {
        Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getDouble())
                : Optional.empty();
    }

    @Override
    public String force(String name) {
        return get(name).orElseThrow(() -> missingProperty(name));
    }

    @Override
    public int forceInt(String name) {
        return getInt(name).orElseThrow(() -> missingProperty(name));
    }

    @Override
    public double forceDouble(String name) {
        return getDouble(name).orElseThrow(() -> missingProperty(name));
    }

    @Override
    public List<Property> allEntries() {
        return properties;
    }

    @Override
    public Optional<Boolean> getBoolean(String name) {
        Optional<Property> property = findProperty(name);
        return property.isPresent() && property.get().hasValue()
                ? Optional.of(property.get().getBoolean())
                : Optional.empty();
    }

    @Override
    public boolean forceBoolean(String name) {
        return getBoolean(name).orElseThrow(() -> missingProperty(name));
    }

    private IllegalStateException missingProperty(String name) {
        return new IllegalStateException("missing property: " + name);
    }
}
