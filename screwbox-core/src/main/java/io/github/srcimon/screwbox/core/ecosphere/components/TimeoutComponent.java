package io.github.srcimon.screwbox.core.ecosphere.components;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.ecosphere.Component;

import java.io.Serial;

public class TimeoutComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Time timeout;

    public TimeoutComponent(final Time timeout) {
        this.timeout = timeout;
    }
}
