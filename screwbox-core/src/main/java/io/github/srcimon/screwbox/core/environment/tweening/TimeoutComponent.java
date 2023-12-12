package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;
//TODO: remove
public class TimeoutComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Time timeout;

    public TimeoutComponent(final Time timeout) {
        this.timeout = timeout;
    }
}
