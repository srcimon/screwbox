package de.suzufa.screwbox.core.ui;

import java.util.function.Predicate;

import de.suzufa.screwbox.core.Engine;

public abstract class UiMenuItem {

    private final String label;
    private Predicate<Engine> activeCondition = engine -> true;

    protected UiMenuItem(final String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public abstract void onActivate(Engine engine);

    public UiMenuItem activeCondition(Predicate<Engine> condition) {
        activeCondition = condition;
        return this;
    }

    Predicate<Engine> activeCondition() {
        return activeCondition;
    }
}
