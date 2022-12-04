package de.suzufa.screwbox.examples.platformer.components;

import de.suzufa.screwbox.core.entities.Component;

public final class CurrentLevelComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final String name;

    public CurrentLevelComponent(final String name) {
        this.name = name;
    }
}
