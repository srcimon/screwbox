package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Component;

public class ReflectionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Percent opacityModifier;
    public final boolean useWaveEffect;

    public ReflectionComponent() {
        this(Percent.quater(), false);
    }

    public ReflectionComponent(final Percent opacityModifier, boolean useWaveEffect) {
        this.opacityModifier = opacityModifier;
        this.useWaveEffect = useWaveEffect;
    }
}
