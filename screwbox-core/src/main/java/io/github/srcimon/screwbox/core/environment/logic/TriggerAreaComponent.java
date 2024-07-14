package io.github.srcimon.screwbox.core.environment.logic;

import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public final class TriggerAreaComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Archetype triggeredBy;

    public TriggerAreaComponent(final Archetype triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
}
