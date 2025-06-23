package dev.screwbox.core.environment;

import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.rendering.ReflectionComponent;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Describes a specific type of {@link Entity} by its {@link Component}s. E.g. an {@link Entity} containing {@link ReflectionComponent} and {@link TransformComponent}
 * could be named 'water'. Used to search for specific {@link Entity} e.g. via {@link Environment#fetchAll(Archetype)}.
 */
public class Archetype implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Collection<Class<? extends Component>> componentClasses;
    private final int hash;

    /**
     * Creates a new {@link Archetype}. Quite expensive. {@link Archetype}s should be stored in constants.
     *
     * @see #ofSpacial(Class[])
     */
    @SafeVarargs
    public static Archetype of(final Class<? extends Component>... componentClasses) {
        return new Archetype(List.of(componentClasses));
    }

    /**
     * Creates a new {@link Archetype} containing a {@link TransformComponent}. Quite expensive. {@link Archetype}s should be stored in constants.
     *
     * @see #of(Class[])
     */
    @SafeVarargs
    public static Archetype ofSpacial(final Class<? extends Component>... componentClasses) {
        final var components = new ArrayList<>(List.of(componentClasses));
        components.add(TransformComponent.class);
        return new Archetype(components);
    }

    private Archetype(final List<Class<? extends Component>> componentClasses) {
        this.componentClasses = componentClasses;
        this.hash = calculateHash(componentClasses);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Archetype other = (Archetype) obj;
        return other.hash == this.hash;
    }

    private int calculateHash(final List<Class<? extends Component>> componentClasses) {
        if (componentClasses.size() == 1) {
            return List.of(componentClasses.getFirst().getName()).hashCode();
        }
        final List<String> names = new ArrayList<>();
        for (final var componentClass : componentClasses) {
            names.add(componentClass.getName());
        }
        Collections.sort(names);
        return names.hashCode();
    }

    /**
     * Returns true if the given {@link Entity} contains all {@link Component}s of the {@link Archetype}.
     */
    public boolean matches(final Entity entity) {
        return entity.getComponentClasses().containsAll(componentClasses);
    }

    /**
     * Returns true if the given {@link Component} class is contained in this {@link Archetype}.
     */
    public boolean contains(final Class<? extends Component> componentClass) {
        return componentClasses.contains(componentClass);
    }

    @Override
    public String toString() {
        return "Archetype [components=" + componentClasses + "]";
    }

}
