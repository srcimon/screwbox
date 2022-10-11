package de.suzufa.screwbox.core.ui;

import java.util.function.Function;
import java.util.function.Predicate;

import de.suzufa.screwbox.core.Engine;

public abstract class UiMenuItem {

    private final Function<Engine, String> label;
    private Predicate<Engine> activeCondition = engine -> true;

    protected UiMenuItem(final String label) {
        this.label = engine -> label;
    }

    protected UiMenuItem(final Function<Engine, String> dynamicLabel) {
        this.label = dynamicLabel;
    }

    public String label(final Engine engine) {
        return label.apply(engine);
    }

    public abstract void onActivate(Engine engine);

    public UiMenuItem activeCondition(final Predicate<Engine> condition) {
        activeCondition = condition;
        return this;
    }

    public boolean isActive(final Engine engine) {
        return activeCondition.test(engine);
    }
}
