package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;

public final class RegisterToSignalComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final int id;

    public RegisterToSignalComponent(final int id) {
        this.id = id;
    }
}
