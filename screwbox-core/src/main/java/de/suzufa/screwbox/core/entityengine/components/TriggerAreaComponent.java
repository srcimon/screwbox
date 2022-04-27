package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Component;

public final class TriggerAreaComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Archetype triggeredBy;

    public TriggerAreaComponent(final Archetype triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
}
