package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class HorizontalControlComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    //TODO noArgsconstructor ? -> needs enum :(
    public HorizontalControlComponent(final Enum<?> left, final Enum<?> right) {
        this.left = left;
        this.right = right;
    }

    public boolean isEnabled = true;
    public double acceleration;
    public double maxSpeed;
    public Enum<?> left;
    public Enum<?> right;

}
