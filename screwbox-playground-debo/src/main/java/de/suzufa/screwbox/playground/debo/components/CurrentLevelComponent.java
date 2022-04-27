package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entityengine.Component;

public final class CurrentLevelComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final String name;

    public CurrentLevelComponent(final String name) {
        this.name = name;
    }
}
