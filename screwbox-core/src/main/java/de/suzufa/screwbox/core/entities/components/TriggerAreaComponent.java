package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Component;

public final class TriggerAreaComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Archetype triggeredBy;

    public TriggerAreaComponent(final Archetype triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
}
