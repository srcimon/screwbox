package de.suzufa.screwbox.examples.platformer.components;

import de.suzufa.screwbox.core.entities.Component;

public class LabelComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final String label;
    public final int size;

    public LabelComponent(final String label, final int size) {
        this.label = label;
        this.size = size;
    }

}
