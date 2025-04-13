package dev.screwbox.core.environment.light;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class ShadowCasterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final boolean selfShadow;

    public ShadowCasterComponent() {
        this(true);
    }

    public ShadowCasterComponent(final boolean selfShadow) {
        this.selfShadow = selfShadow;
    }

}
