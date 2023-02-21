package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.entities.Component;

import java.io.Serial;

public class ShadowComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final int linkedTo;

    public ShadowComponent(final int linkedTo) {
        this.linkedTo = linkedTo;
    }
}
