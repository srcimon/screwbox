package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

//TODO add to reference docs
public class JumpControlComponent implements Component {

    public Enum<?> keyAlias;
    public double acceleration;
    public boolean isEnabled = true;
    public Time lastActivation = Time.unset();

    public JumpControlComponent(final Enum<?> keyAlias) {
        this.keyAlias = keyAlias;
    }
}