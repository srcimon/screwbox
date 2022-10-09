package de.suzufa.screwbox.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.suzufa.screwbox.core.Engine;

public abstract class UiMenuItem {

    private final String label;
    private List<Function<Engine, Boolean>> activeConditions = new ArrayList<>();

    protected UiMenuItem(final String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public abstract void onActivate(Engine engine);

    public UiMenuItem addActiveCondition(Function<Engine, Boolean> condition) {
        activeConditions.add(condition);
        return this;
    }

    List<Function<Engine, Boolean>> activeConditions() {
        return activeConditions;
    }
}
