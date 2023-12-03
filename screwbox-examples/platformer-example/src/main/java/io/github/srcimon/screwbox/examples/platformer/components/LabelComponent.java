package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.ecosphere.Component;

import java.io.Serial;

public class LabelComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final String label;
    public final int size;

    public LabelComponent(final String label, final int size) {
        this.label = label;
        this.size = size;
    }

}
