package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Color;

import java.io.Serial;

public class BloomComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Color color;

    public BloomComponent() {
        this(Color.WHITE);
    }

    public BloomComponent(final Color color) {
        this.color = color;
    }
}
