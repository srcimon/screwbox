package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

//TODO add to reference docs
public class JumpControlComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Enum<?> keyAlias;
    public double acceleration;
    public boolean isEnabled = true;
    public Time lastActivation = Time.unset();

    public JumpControlComponent(final Enum<?> keyAlias) {
        this.keyAlias = keyAlias;
    }
}