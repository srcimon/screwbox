package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.MovementTargetComponent;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.isNull;

public final class Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<Class<? extends Component>, Component> components = new HashMap<>();
    private final Integer id;
    private transient List<EntityListener> listeners;
    private String name;
    private TransformComponent tranform;

    public Entity() {
        this.id = null;
    }

    public Entity(final Integer id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Entity(final String name) {
        this(null, name);
    }

    public Entity(final Integer id) {
        this.id = id;
    }

    public Entity name(final String name) {
        this.name = name;
        return this;
    }

    public Optional<String> name() {
        return Optional.ofNullable(name);
    }

    public Optional<Integer> id() {
        return Optional.ofNullable(id);
    }

    public Entity add(final Component... components) {
        for (final var component : components) {
            add(component);
        }
        return this;
    }

    public <T extends Component> Entity addCustomized(T component, Consumer<T> customizing) {
        customizing.accept(component);
        return add(component);
    }

    public Entity add(final Component component) {
        final var componentClass = component.getClass();
        if (components.containsKey(componentClass)) {
            throw new IllegalArgumentException("component already present: " + componentClass.getSimpleName());
        }
        components.put(componentClass, component);
        if (component instanceof TransformComponent transformComponent) {
            tranform = transformComponent;
        }
        final var event = new EntityEvent(this);
        for (final var listener : getListeners()) {
            listener.componentAdded(event);
        }
        return this;
    }

    /**
     * Returns a {@link Component} of the specified class attached to this {@link Entity}. Will return null if no such
     * {@link Component} is present.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T get(final Class<T> componentClass) {
        return (T) components.get(componentClass);
    }

    /**
     * Returns all {@link Component components}  attached to this {@link Entity}.
     */
    public Collection<Component> getAll() {
        return components.values();
    }

    /**
     * Returns the count of all present {@link Component components} attached to this {@link Entity}.
     */
    public int componentCount() {
        return components.size();
    }

    /**
     * Returns a collection of all present {@link Component} classes attached to this {@link Entity}.
     */
    public Collection<Class<? extends Component>> getComponentClasses() {
        return components.keySet();
    }

    /**
     * Registers the specified {@link EntityListener} to receive Events for this
     * {@link Entity}. The {@link EntityListener} is notified on adding or removing
     * {@link Component}. {@link EntityListener}s don't survive serialization of the {@link Entity},
     * because it's very unlikely that a listener-class can be serialized itself.
     */
    public void registerListener(final EntityListener listener) {
        getListeners().add(listener);
    }

    /**
     * Checks if the specified {@link Component}-class is present in this
     * {@link Entity}.
     *
     * @param componentClass the {@link Component}-class to check
     * @return {@code true} if the {@link Component}-class is present
     */
    public boolean hasComponent(final Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
    }

    /**
     * Removes a {@link Component} of the given class. Won't do anything, if no {@link Component} of specified class is present.
     */
    public void remove(final Class<? extends Component> componentClass) {
        components.remove(componentClass);
        if (TransformComponent.class.equals(componentClass)) {
            tranform = null;
        }
        final var event = new EntityEvent(this);
        for (final var listener : getListeners()) {
            listener.componentRemoved(event);
        }
    }

    /**
     * Returns {@code true} if the {@link Entity} has no {@link Component}.
     */
    public boolean isEmpty() {
        return componentCount() == 0;
    }

    @Override
    public String toString() {
        return String.format("Entity[%s%scomponents=%s]",
                id == null ? "" : "id='" + id + "', ",
                name == null ? "" : "name='" + name + "', ",
                components.isEmpty() ? "none" : components.size());
    }

    /**
     * Returns {@link Bounds#origin()} if {@link Entity} has {@link TransformComponent}.
     *
     * @throws IllegalStateException if {@link Entity} has no {@link TransformComponent}
     */
    public Vector origin() {
        return bounds().origin();
    }

    /**
     * Returns {@link Bounds#position()} if {@link Entity} has {@link TransformComponent}.
     *
     * @throws IllegalStateException if {@link Entity} has no {@link TransformComponent}
     */
    public Vector position() {
        return bounds().position();
    }

    /**
     * Returns {@link Bounds} if {@link Entity} has {@link TransformComponent}.
     *
     * @throws IllegalStateException if {@link Entity} has no {@link TransformComponent}
     */
    public Bounds bounds() {
        if (isNull(tranform)) {
            throw new IllegalStateException("entity has no TransformComponent");
        }
        return tranform.bounds;
    }

    /**
     * Moves an {@link Entity} to the specified {@link Bounds#position()}.
     *
     * @throws IllegalStateException if {@link Entity} has no {@link TransformComponent}
     * @see #moveBy(Vector)
     */
    public void moveTo(final Vector position) {
        tranform.bounds = bounds().moveTo(position);
    }

    /**
     * Moves an {@link Entity} by the specified delta.
     *
     * @throws IllegalStateException if {@link Entity} has no {@link TransformComponent}
     * @see #moveTo(Vector)
     */
    public void moveBy(final Vector delta) {
        tranform.bounds = bounds().moveBy(delta);
    }

    /**
     * Adds a {@link Component} if there is not already another instance of that {@link Component} present.
     */
    public void addIfNotPresent(final Component component) {
        if (!hasComponent(component.getClass())) {
            add(component);
        }
    }

    //TODO changelog
    //TODO test
    //TODO javadoc
    public void addOrReplace(final Component component) {
        remove(component.getClass());
        add(component);
    }

    private List<EntityListener> getListeners() {
        if (isNull(listeners)) {
            listeners = new ArrayList<>();
        }
        return listeners;
    }

}
