package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.ecosphere.Component;

import java.io.Serial;

public class ShadowComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final int linkedTo;

    public ShadowComponent(final int linkedTo) {
        this.linkedTo = linkedTo;
    }
}
