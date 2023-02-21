package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.Component;

import java.io.Serial;

public class TimeoutComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Time timeout;

    public TimeoutComponent(final Time timeout) {
        this.timeout = timeout;
    }
}
