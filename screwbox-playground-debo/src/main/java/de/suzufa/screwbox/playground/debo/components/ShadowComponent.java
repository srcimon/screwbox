package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entities.Component;

public class ShadowComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final int linkedTo;

    public ShadowComponent(final int linkedTo) {
        this.linkedTo = linkedTo;
    }
}
