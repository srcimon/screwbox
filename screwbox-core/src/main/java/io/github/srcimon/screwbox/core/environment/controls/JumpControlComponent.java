package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

public class JumpControlComponent implements Component {

    public Enum<?> keyAlias;
    public double acceleration;
    public boolean isEnabled = true;
    public Time lastActivation = Time.unset();
//TODO unify mouse and keyboard aliases?
    public JumpControlComponent(final Enum<?> keyAlias) {
        this.keyAlias = keyAlias;
    }
}
