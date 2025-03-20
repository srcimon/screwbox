package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Cache;

import java.io.Serial;

//TODO document
public class TweenShaderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final boolean invert;

    public TweenShaderComponent() {
        this(false);
    }

    public TweenShaderComponent(boolean invert) {
        this.invert = invert;
    }
}
