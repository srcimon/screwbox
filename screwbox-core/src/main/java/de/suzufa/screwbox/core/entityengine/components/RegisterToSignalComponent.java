package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.entityengine.Component;

public final class RegisterToSignalComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final int id;

    public RegisterToSignalComponent(final int id) {
        this.id = id;
    }
}
