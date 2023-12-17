package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Sprite;

import java.io.Serial;

public class BackgroundHolderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Sprite background;
}
