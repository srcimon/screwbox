package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.Component;

public class ReflectionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Percentage opacityModifier;
    public final boolean useWaveEffect;

    public ReflectionComponent() {
        this(Percentage.quater(), false);
    }

    public ReflectionComponent(final Percentage opacityModifier, boolean useWaveEffect) {
        this.opacityModifier = opacityModifier;
        this.useWaveEffect = useWaveEffect;
    }
}
