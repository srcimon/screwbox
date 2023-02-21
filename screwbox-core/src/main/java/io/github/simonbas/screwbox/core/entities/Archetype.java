package io.github.simonbas.screwbox.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Archetype implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Collection<Class<? extends Component>> componentClasses;
    private final int hash;

    @SafeVarargs
    public static Archetype of(final Class<? extends Component>... componentClasses) {
        return new Archetype(List.of(componentClasses));
    }

    private Archetype(final Collection<Class<? extends Component>> componentClasses) {
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

    private int calculateHash(final Collection<Class<? extends Component>> componentClasses) {
        final List<String> names = new ArrayList<>();
        for (final var componentClass : componentClasses) {
            names.add(componentClass.getName());
        }
        Collections.sort(names);
        return names.hashCode();
    }

    public boolean matches(final Entity entity) {
        return entity.getComponentClasses().containsAll(componentClasses);
    }

    public boolean contains(final Class<? extends Component> componentClass) {
        return componentClasses.contains(componentClass);
    }

    @Override
    public String toString() {
        return "Archetype [componentClasses=" + componentClasses + "]";
    }

}
