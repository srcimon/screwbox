package io.github.srcimon.screwbox.examples.animation.components;

import io.github.srcimon.screwbox.core.entities.Component;

import java.io.Serial;
import java.util.Random;

public class DotComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public int diameter = new Random().nextInt(3, 8);
}
