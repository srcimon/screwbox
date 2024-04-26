package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ReflectionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Percent opacityModifier;
    public int drawOrder;

    public ReflectionComponent() {
        this(Percent.quater());
    }

    public ReflectionComponent(final Percent opacityModifier) {
        this(opacityModifier, Integer.MAX_VALUE);
    }

    public ReflectionComponent(final Percent opacityModifier, final int drawOrder) {
        this.opacityModifier = opacityModifier;
        this.drawOrder = drawOrder;
    }
}
