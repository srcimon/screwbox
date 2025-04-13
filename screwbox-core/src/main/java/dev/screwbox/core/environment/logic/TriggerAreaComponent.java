package dev.screwbox.core.environment.logic;

import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

public final class TriggerAreaComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Archetype triggeredBy;
    public boolean isTriggered;

    public TriggerAreaComponent(final Archetype triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
}
